package com.betopia.hrm.services.leaves.leaveCategory.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveCategory;
import com.betopia.hrm.domain.leave.entity.LeaveGroup;
import com.betopia.hrm.domain.leave.exception.leaveCategory.LeaveCategoryNotFoundException;
import com.betopia.hrm.domain.leave.repository.LeaveCategoryRepository;
import com.betopia.hrm.domain.leave.request.LeaveCategoryRequest;
import com.betopia.hrm.services.base.BaseService;
import com.betopia.hrm.services.base.HrmFileServiceImpl;
import com.betopia.hrm.services.leaves.leaveCategory.LeaveCategoryService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveCategoryServiceImpl extends HrmFileServiceImpl<LeaveCategory> implements LeaveCategoryService {

    private final LeaveCategoryRepository leaveCategoryRepository;

    public Class<LeaveCategory> getEntityClass() {
        return LeaveCategory.class;
    }

    public LeaveCategoryServiceImpl(LeaveCategoryRepository leaveCategoryRepository) {
        this.leaveCategoryRepository = leaveCategoryRepository;
    }

    @Override
    public PaginationResponse<LeaveCategory> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveCategory> leaveCategoryPage = leaveCategoryRepository.findAll(pageable);

        List<LeaveCategory> leaveCategories = leaveCategoryPage.getContent();

        PaginationResponse<LeaveCategory> response = new PaginationResponse<>();
        response.setData(leaveCategories);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave categories fetched successfully");

        Links links = Links.fromPage(leaveCategoryPage, "/leave-categories");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leaveCategoryPage, "/leave-categories");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeaveCategory> getAll() {
        var leaveCategories =  leaveCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        List<LeaveCategory> roots = leaveCategories.stream()
                .filter(c -> c.getParent() == null)
                .collect(Collectors.toList());
        return roots;
    }

    @Override
    public LeaveCategory store(LeaveCategoryRequest request) {
        LeaveCategory leaveCategory = new LeaveCategory();

        leaveCategory.setName(request.name());
        if (request.parentId() != null) {
            LeaveCategory parent = leaveCategoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found with id: " + request.parentId()));
            leaveCategory.setParent(parent);
        }

        leaveCategory.setStatus(request.status());

        leaveCategory = leaveCategoryRepository.save(leaveCategory);

        return leaveCategory;
    }

    @Override
    public LeaveCategory show(Long leaveCategoryId) {
        LeaveCategory leaveCategory = leaveCategoryRepository.findById(leaveCategoryId)
                .orElseThrow(() -> new LeaveCategoryNotFoundException("Leave category not found with id: " + leaveCategoryId));
        return leaveCategory;
    }

    @Override
    public LeaveCategory update(Long leaveCategoryId, LeaveCategoryRequest request) {
        LeaveCategory leaveCategory = leaveCategoryRepository.findById(leaveCategoryId)
                .orElseThrow(() -> new RuntimeException("LeaveCategory not found with id: " + leaveCategoryId));

        // Update name
        leaveCategory.setName(request.name() != null ? request.name() : leaveCategory.getName());

        // Update parent
        if (request.parentId() != null) {
            LeaveCategory parent = leaveCategoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found with id: " + request.parentId()));
            leaveCategory.setParent(parent); // self-parenting check
        } else {
            leaveCategory.setParent(null); // যদি parentId null হয়, parent remove করা হবে
        }

        // Status update
        leaveCategory.setStatus(request.status() != null ? request.status() : leaveCategory.getStatus());


        // Save
        leaveCategory = leaveCategoryRepository.save(leaveCategory);

        return leaveCategory;
    }

    @Override
    public void destroy(Long leaveCategoryId) {
        LeaveCategory leaveCategory = leaveCategoryRepository.findById(leaveCategoryId)
                .orElseThrow(() -> new RuntimeException("LeaveCategory not found with id: " + leaveCategoryId));

        leaveCategoryRepository.delete(leaveCategory);
    }

    /**
     * Import with Parent-Child relationship support
     */
    public List<LeaveCategory> importAndSave(MultipartFile file) throws Exception {
        List<LeaveCategory> categories = new ArrayList<>();
        Map<Long, LeaveCategory> idMap = new HashMap<>(); // Map by temporary Excel ID
        Map<Long, Long> parentIdMap = new HashMap<>(); // Track which category has which parent ID

        System.out.println("=== IMPORT STARTED ===");

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) return categories;

            // Read header
            Row headerRow = rows.next();
            Map<String, Integer> headerMap = new HashMap<>();

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String headerName = cell.getStringCellValue().trim().toLowerCase();
                    headerMap.put(headerName, i);
                    System.out.println("Column " + i + ": '" + headerName + "'");
                }
            }

            System.out.println("Header Map: " + headerMap);

            // First pass: Create all categories and track parent IDs
            System.out.println("\n=== FIRST PASS: Creating categories ===");
            int rowNum = 1;

            while (rows.hasNext()) {
                Row row = rows.next();
                rowNum++;

                // Get temporary ID from Excel (optional, but helpful)
                Long tempId = null;
                Integer idIdx = headerMap.get("id");
                if (idIdx != null) {
                    Cell idCell = row.getCell(idIdx);
                    if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
                        tempId = (long) idCell.getNumericCellValue();
                    }
                }

                // Get name
                Integer nameIdx = headerMap.get("name");
                if (nameIdx == null) {
                    System.out.println("Row " + rowNum + ": SKIP - 'name' column not found!");
                    continue;
                }

                Cell nameCell = row.getCell(nameIdx);
                if (nameCell == null || nameCell.getCellType() != CellType.STRING) {
                    System.out.println("Row " + rowNum + ": SKIP - name cell is null or not STRING");
                    continue;
                }

                String name = nameCell.getStringCellValue().trim();
                if (name.isEmpty()) {
                    System.out.println("Row " + rowNum + ": SKIP - name is empty");
                    continue;
                }

                // Create category
                LeaveCategory category = new LeaveCategory();
                category.setName(name);


                Integer activeIdx = headerMap.get("active");
                if (activeIdx != null) {
                    Cell activeCell = row.getCell(activeIdx);
                    if (activeCell != null) {
                        if (activeCell.getCellType() == CellType.BOOLEAN) {
                            category.setStatus(activeCell.getBooleanCellValue());
                        } else if (activeCell.getCellType() == CellType.STRING) {
                            String val = activeCell.getStringCellValue().trim().toLowerCase();
                            category.setStatus(val.equals("true") || val.equals("yes") || val.equals("active"));
                        } else if (activeCell.getCellType() == CellType.NUMERIC) {
                            category.setStatus(activeCell.getNumericCellValue() == 1);
                        } else {
                            category.setStatus(true);
                        }
                    } else {
                        category.setStatus(true);
                    }
                } else {
                    category.setStatus(true);
                }

                // Get parent_id (numeric)
                Long parentId = null;
                Integer parentIdx = headerMap.get("parent_id");
                if (parentIdx != null) {
                    Cell parentCell = row.getCell(parentIdx);
                    if (parentCell != null) {
                        if (parentCell.getCellType() == CellType.NUMERIC) {
                            parentId = (long) parentCell.getNumericCellValue();
                            System.out.println("Row " + rowNum + ": Parent ID = " + parentId);
                        } else if (parentCell.getCellType() == CellType.STRING) {
                            String parentStr = parentCell.getStringCellValue().trim();
                            if (!parentStr.isEmpty()) {
                                try {
                                    parentId = Long.parseLong(parentStr);
                                    System.out.println("Row " + rowNum + ": Parent ID (from string) = " + parentId);
                                } catch (NumberFormatException e) {
                                    System.out.println("Row " + rowNum + ": Invalid parent ID: " + parentStr);
                                }
                            }
                        }
                    }
                }

                categories.add(category);

                // Store mapping
                if (tempId != null) {
                    idMap.put(tempId, category);
                    if (parentId != null) {
                        parentIdMap.put(tempId, parentId);
                    }
                    System.out.println("Row " + rowNum + ": ✓ Added - " + name + " (Temp ID: " + tempId + ", Parent ID: " + parentId + ")");
                } else {
                    // If no ID column, use index
                    long autoId = (long) categories.size();
                    idMap.put(autoId, category);
                    if (parentId != null) {
                        parentIdMap.put(autoId, parentId);
                    }
                    System.out.println("Row " + rowNum + ": ✓ Added - " + name + " (Auto ID: " + autoId + ", Parent ID: " + parentId + ")");
                }
            }

            System.out.println("\nTotal categories created: " + categories.size());
            System.out.println("Parent relationships to set: " + parentIdMap.size());

            if (categories.isEmpty()) {
                System.out.println("WARNING: No categories to save!");
                return categories;
            }

            // Save all categories WITHOUT parent first
            System.out.println("\n=== Saving categories to database ===");
            List<LeaveCategory> saved = leaveCategoryRepository.saveAll(categories);
            System.out.println("✓ Saved " + saved.size() + " categories");

            // Update idMap with saved entities (now they have real database IDs)
            for (int i = 0; i < saved.size(); i++) {
                LeaveCategory cat = saved.get(i);
                // Update the map - key is still temp ID, value is now saved entity with DB ID
                System.out.println("  " + cat.getName() + " saved with DB ID: " + cat.getId());
            }

            // Second pass: Set parent relationships
            System.out.println("\n=== SECOND PASS: Setting parent relationships ===");

            for (Map.Entry<Long, Long> entry : parentIdMap.entrySet()) {
                Long childTempId = entry.getKey();
                Long parentTempId = entry.getValue();

                LeaveCategory child = idMap.get(childTempId);
                LeaveCategory parent = idMap.get(parentTempId);

                if (child != null && parent != null) {
                    child.setParent(parent);
                    System.out.println("✓ Set parent: " + child.getName() + " → parent: " + parent.getName() + " (Parent DB ID: " + parent.getId() + ")");
                } else {
                    System.out.println("✗ Failed to set parent for temp ID " + childTempId + " (parent temp ID: " + parentTempId + ")");
                    if (child == null) System.out.println("  Child not found!");
                    if (parent == null) System.out.println("  Parent not found!");
                }
            }

            // Save with parent relationships
            if (!parentIdMap.isEmpty()) {
                System.out.println("\n=== Saving parent relationships ===");
                saved = leaveCategoryRepository.saveAll(saved);
                System.out.println("✓ Updated with parent relationships");
            }

            System.out.println("\n=== IMPORT COMPLETE ===");
            System.out.println("Final count: " + saved.size());

            // Print final structure
            System.out.println("\n=== Final Structure ===");
            for (LeaveCategory cat : saved) {
                String parentInfo = cat.getParent() != null
                        ? "Parent: " + cat.getParent().getName() + " (ID: " + cat.getParent().getId() + ")"
                        : "No parent (Root category)";
                System.out.println("  " + cat.getName() + " (ID: " + cat.getId() + ") - " + parentInfo);
            }

            return saved;
        }
    }

    public byte[] exportActiveToExcel() throws Exception {
        List<LeaveCategory> categories = leaveCategoryRepository.findByStatus(true);
        return exportToExcel(categories).toByteArray();
    }

    public byte[] export(boolean type, String format) throws Exception {
        List<LeaveCategory> categories;
        if (type) {
            categories = leaveCategoryRepository.findByStatus(true);  // true = export only active
        } else {
            categories = leaveCategoryRepository.findAll(); // false = export all records

        }
        if ("excel".equalsIgnoreCase(format)) {
            return exportToExcel(categories).toByteArray();
        } else if ("csv".equalsIgnoreCase(format)) {
            return exportToCsv(categories).toByteArray();
        } else if ("pdf".equalsIgnoreCase(format)) {
            return exportToPdf(categories).toByteArray();
        } else {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }


}
