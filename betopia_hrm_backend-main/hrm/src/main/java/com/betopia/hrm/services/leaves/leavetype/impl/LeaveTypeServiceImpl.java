package com.betopia.hrm.services.leaves.leavetype.impl;


import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRepository;
import com.betopia.hrm.domain.leave.request.LeaveTypeRequest;
import com.betopia.hrm.services.base.HrmFileServiceImpl;
import com.betopia.hrm.services.leaves.leavetype.LeaveTypeService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class LeaveTypeServiceImpl extends HrmFileServiceImpl<LeaveType> implements LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;


    public LeaveTypeServiceImpl(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    public Class<LeaveType> getEntityClass() {
        return LeaveType.class;
    }


    @Override
    public PaginationResponse<LeaveType> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveType> leaveTypePage = leaveTypeRepository.findAll(pageable);
        List<LeaveType> leaveTypes = leaveTypePage.getContent();

        PaginationResponse<LeaveType> response = new PaginationResponse<>();
        response.setData(leaveTypes);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave types fetched successfully");

        Links links = Links.fromPage(leaveTypePage, "/leave-types");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leaveTypePage, "/leave-types");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {

        return leaveTypeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public List<LeaveType> getAllLeaveTypeByStatus(Boolean status) {
        return leaveTypeRepository.findByStatus(status);
    }

    @Override
    public LeaveType getLeaveTypeById(Long id) {

        return leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));
    }

    @Override
    public LeaveType store(LeaveTypeRequest request) {

        LeaveType leaveType = new LeaveType();
        leaveType.setName(request.name());
        leaveType.setStatus(request.status());
        leaveType.setCode(request.code());
        leaveType.setDescription(request.description());

        return leaveTypeRepository.save(leaveType);
    }

    @Override
    public LeaveType updateLeaveType(Long id, LeaveTypeRequest request) {

        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));

        leaveType.setName(request.name() != null ? request.name() : leaveType.getName());
        leaveType.setCode(request.code() != null ? request.code() : leaveType.getCode());
        leaveType.setStatus(request.status() != null ? request.status() : leaveType.getStatus());
        leaveType.setDescription(request.description() != null ? request.description() : leaveType.getDescription());
        leaveType.setLastModifiedDate(LocalDateTime.now());

        return leaveTypeRepository.save(leaveType);
    }

    @Override
    public void deleteLeaveType(Long id) {

        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveType not found with id: " + id));

        leaveType.setDeletedAt(LocalDateTime.now());

        leaveTypeRepository.delete(leaveType);

    }

    /**
     * Import with Parent-Child relationship support
     */
    @Override
    public List<LeaveType> importAndSave(MultipartFile file) throws Exception {
        List<LeaveType> leaveTypes = new ArrayList<>();

        System.out.println("=== IMPORT STARTED ===");

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) return leaveTypes;

            // --- Read header row ---
            Row headerRow = rows.next();
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String header = cell.getStringCellValue().trim().toLowerCase();
                    headerMap.put(header, i);
                }
            }
            System.out.println("Header Map: " + headerMap);

            // --- Read all rows ---
            int rowNum = 1;
            while (rows.hasNext()) {
                Row row = rows.next();
                rowNum++;

                LeaveType leaveType = new LeaveType();

                // name
                Integer nameIdx = headerMap.get("name");
                if (nameIdx == null) {
                    System.out.println("Row " + rowNum + ": Missing 'name' column!");
                    continue;
                }
                Cell nameCell = row.getCell(nameIdx);
                if (nameCell == null || nameCell.getCellType() != CellType.STRING) {
                    System.out.println("Row " + rowNum + ": Invalid name cell");
                    continue;
                }
                leaveType.setName(nameCell.getStringCellValue().trim());

                // code
                Integer codeIdx = headerMap.get("code");
                if (codeIdx != null) {
                    Cell codeCell = row.getCell(codeIdx);
                    if (codeCell != null && codeCell.getCellType() == CellType.STRING) {
                        leaveType.setCode(codeCell.getStringCellValue().trim());
                    } else if (codeCell != null && codeCell.getCellType() == CellType.NUMERIC) {
                        leaveType.setCode(String.valueOf((long) codeCell.getNumericCellValue()));
                    } else {
                        leaveType.setCode("N/A");
                    }
                } else {
                    leaveType.setCode("N/A");
                }

                // description
                Integer descIdx = headerMap.get("description");
                if (descIdx != null) {
                    Cell descCell = row.getCell(descIdx);
                    if (descCell != null && descCell.getCellType() == CellType.STRING) {
                        leaveType.setDescription(descCell.getStringCellValue().trim());
                    }
                }

                // active (boolean)
                Integer activeIdx = headerMap.get("active");
                if (activeIdx != null) {
                    Cell activeCell = row.getCell(activeIdx);
                    if (activeCell != null) {
                        if (activeCell.getCellType() == CellType.BOOLEAN) {
                            leaveType.setStatus(activeCell.getBooleanCellValue());
                        } else if (activeCell.getCellType() == CellType.STRING) {
                            String val = activeCell.getStringCellValue().trim().toLowerCase();
                            leaveType.setStatus(val.equals("true") || val.equals("yes") || val.equals("active"));
                        } else if (activeCell.getCellType() == CellType.NUMERIC) {
                            leaveType.setStatus(activeCell.getNumericCellValue() == 1);
                        } else {
                            leaveType.setStatus(true);
                        }
                    } else {
                        leaveType.setStatus(true);
                    }
                } else {
                    leaveType.setStatus(true);
                }

                leaveTypes.add(leaveType);
                System.out.println("✓ Row " + rowNum + ": " + leaveType.getName() + " added");
            }

            if (leaveTypes.isEmpty()) {
                System.out.println("WARNING: No leaveTypes to save!");
                return leaveTypes;
            }

            // --- Save all ---
            System.out.println("\nSaving to DB...");
            List<LeaveType> saved = leaveTypeRepository.saveAll(leaveTypes);
            System.out.println("✓ Saved " + saved.size() + " records");

            System.out.println("=== IMPORT COMPLETE ===");
            return saved;
        }
    }

    @Override
    public byte[] export(Boolean type, String format) throws Exception {
        List<LeaveType> leaveTypes;

        if (type) {
            leaveTypes = leaveTypeRepository.findByStatus(true); // true = export only active
        } else {
            leaveTypes = leaveTypeRepository.findAll(); // false = export all records
        }

        if ("excel".equalsIgnoreCase(format)) {
            return exportToExcel(leaveTypes).toByteArray();
        } else if ("csv".equalsIgnoreCase(format)) {
            return exportToCsv(leaveTypes).toByteArray();
        } else if ("pdf".equalsIgnoreCase(format)) {
            return exportToPdf(leaveTypes).toByteArray();
        } else {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }


}
