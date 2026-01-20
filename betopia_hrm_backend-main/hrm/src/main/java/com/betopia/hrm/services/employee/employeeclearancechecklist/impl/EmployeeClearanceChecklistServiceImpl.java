package com.betopia.hrm.services.employee.employeeclearancechecklist.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.EmployeeClearanceChecklistDTO;
import com.betopia.hrm.domain.dto.employee.mapper.EmployeeClearanceChecklistMapper;
import com.betopia.hrm.domain.employee.entity.EmployeeClearanceChecklist;
import com.betopia.hrm.domain.employee.repository.EmployeeClearanceChecklistRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.ExitClearanceItemRepository;
import com.betopia.hrm.domain.employee.request.EmployeeClearanceChecklistRequest;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.employeeclearancechecklist.EmployeeClearanceChecklistService;
import com.betopia.hrm.webapp.util.UploadFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class EmployeeClearanceChecklistServiceImpl implements EmployeeClearanceChecklistService {

    private final EmployeeClearanceChecklistRepository employeeClearanceChecklistRepository;
    private final EmployeeClearanceChecklistMapper employeeClearanceChecklistMapper;
    private final ExitClearanceItemRepository exitClearanceItemRepository;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final UserRepository userRepository;
    private final UploadFiles uploadFiles;

    public EmployeeClearanceChecklistServiceImpl(EmployeeClearanceChecklistRepository employeeClearanceChecklistRepository,
                                                 EmployeeClearanceChecklistMapper employeeClearanceChecklistMapper,
                                                 ExitClearanceItemRepository exitClearanceItemRepository,
                                                 EmployeeSeparationsRepository employeeSeparationsRepository,
                                                 UserRepository userRepository, UploadFiles uploadFiles) {
        this.employeeClearanceChecklistRepository = employeeClearanceChecklistRepository;
        this.employeeClearanceChecklistMapper = employeeClearanceChecklistMapper;
        this.exitClearanceItemRepository = exitClearanceItemRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.userRepository = userRepository;
        this.uploadFiles = uploadFiles;
    }

    @Override
    public PaginationResponse<EmployeeClearanceChecklistDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<EmployeeClearanceChecklist> clearanceItemPage = employeeClearanceChecklistRepository.findAll(pageable);

        // Get content from page
        List<EmployeeClearanceChecklist> employeeClearanceChecklist = clearanceItemPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<EmployeeClearanceChecklistDTO> itemDTOS = employeeClearanceChecklistMapper.toDTOList(employeeClearanceChecklist);

        // Create pagination response
        PaginationResponse<EmployeeClearanceChecklistDTO> response = new PaginationResponse<>();
        response.setData(itemDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All exit clearance checklist successfully");

        // Set links
        Links links = Links.fromPage(clearanceItemPage, "/checklist");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(clearanceItemPage, "/checklist");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeClearanceChecklistDTO> getAll() {
        List<EmployeeClearanceChecklist> employeeClearanceChecklist = employeeClearanceChecklistRepository.findAll();
        return employeeClearanceChecklistMapper.toDTOList(employeeClearanceChecklist);
    }

    @Override
    public EmployeeClearanceChecklistDTO store(EmployeeClearanceChecklistRequest request) {
        return null;
    }

    @Override
    public EmployeeClearanceChecklistDTO show(Long id) {
        EmployeeClearanceChecklist employeeClearanceChecklist = employeeClearanceChecklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance checklist not found with id: " + id));
        return employeeClearanceChecklistMapper.toDTO(employeeClearanceChecklist);
    }


    @Override
    public void destroy(Long id) {
        EmployeeClearanceChecklist employeeClearanceChecklist = employeeClearanceChecklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance checklist not found"));

        employeeClearanceChecklistRepository.delete(employeeClearanceChecklist);
    }

    @Override
    public EmployeeClearanceChecklistDTO saveChecklist(EmployeeClearanceChecklistRequest request, List<MultipartFile> files) {
        EmployeeClearanceChecklist employeeClearanceChecklist = new EmployeeClearanceChecklist();

        employeeClearanceChecklist.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation id not found")));

        employeeClearanceChecklist.setClearanceItem(exitClearanceItemRepository.findById(request.clearanceItemId())
                .orElseThrow(() -> new RuntimeException("Clearance item id not found")));

        employeeClearanceChecklist.setAssignedTo(userRepository.findById(request.assignedTo())
                .orElseThrow(() -> new RuntimeException("Assigned id not found")));

        employeeClearanceChecklist.setClearedBy(userRepository.findById(request.clearedBy())
                .orElseThrow(() -> new RuntimeException("Cleared by id not found")));

        employeeClearanceChecklist.setDepartments(request.department());
        employeeClearanceChecklist.setRemarks(request.remarks());
        employeeClearanceChecklist.setEvidencePath(request.evidencePath());
        employeeClearanceChecklist.setClearedDate(request.clearedDate());
        employeeClearanceChecklist.setSlaDeadline(request.slaDeadline());
        employeeClearanceChecklist.setIsOverdue(request.isOverdue());

        // Save entity
        EmployeeClearanceChecklist saved = employeeClearanceChecklistRepository.save(employeeClearanceChecklist);


        if (files != null && !files.isEmpty()) {
            String folder = "checklist/" + saved.getId();
            StringBuilder urls = new StringBuilder();

            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null;
                });
            }

            saved.setEvidencePath(urls.toString());
            saved = employeeClearanceChecklistRepository.save(saved);
        }

        // Convert Entity to DTO and return
        return employeeClearanceChecklistMapper.toDTO(saved);
    }

    @Override
    public EmployeeClearanceChecklistDTO updateChecklist(Long id, EmployeeClearanceChecklistRequest request, List<MultipartFile> files) {
        EmployeeClearanceChecklist employeeClearanceChecklist = new EmployeeClearanceChecklist();

        employeeClearanceChecklist.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation id not found")));

        employeeClearanceChecklist.setClearanceItem(exitClearanceItemRepository.findById(request.clearanceItemId())
                .orElseThrow(() -> new RuntimeException("Clearance item id not found")));

        employeeClearanceChecklist.setAssignedTo(userRepository.findById(request.assignedTo())
                .orElseThrow(() -> new RuntimeException("Assigned id not found")));

        employeeClearanceChecklist.setClearedBy(userRepository.findById(request.clearedBy())
                .orElseThrow(() -> new RuntimeException("Cleared by id not found")));

        employeeClearanceChecklist.setDepartments(request.department());
        employeeClearanceChecklist.setRemarks(request.remarks());
        employeeClearanceChecklist.setEvidencePath(request.evidencePath());
        employeeClearanceChecklist.setClearedDate(request.clearedDate());
        employeeClearanceChecklist.setSlaDeadline(request.slaDeadline());
        employeeClearanceChecklist.setIsOverdue(request.isOverdue());

        if (files != null && !files.isEmpty()) {
            String folder = "employee-separations/" + employeeClearanceChecklist.getId();
            StringBuilder urls = new StringBuilder();

            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null;
                });
            }

            if (employeeClearanceChecklist.getEvidencePath() != null && !employeeClearanceChecklist.getEvidencePath().isEmpty()) {
                urls.insert(0, employeeClearanceChecklist.getEvidencePath() + ",");
            }
            employeeClearanceChecklist.setEvidencePath(urls.toString());
        }
        EmployeeClearanceChecklist saved = employeeClearanceChecklistRepository.save(employeeClearanceChecklist);

        return employeeClearanceChecklistMapper.toDTO(saved);
    }
}
