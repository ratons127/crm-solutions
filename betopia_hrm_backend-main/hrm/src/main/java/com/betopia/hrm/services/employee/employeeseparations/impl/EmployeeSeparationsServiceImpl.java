package com.betopia.hrm.services.employee.employeeseparations.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeSeparations;
import com.betopia.hrm.domain.employee.enums.SeparationStatus;
import com.betopia.hrm.domain.employee.exception.employeeseparations.EmployeeSeparationsNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.request.EmployeeSeparationsRequest;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.employeeseparations.EmployeeSeparationsService;
import com.betopia.hrm.webapp.util.UploadFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeSeparationsServiceImpl implements EmployeeSeparationsService {

    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UploadFiles uploadFiles;

    public EmployeeSeparationsServiceImpl(EmployeeSeparationsRepository employeeSeparationsRepository,
                                                EmployeeRepository employeeRepository, UserRepository userRepository,
                                          UploadFiles uploadFiles) {
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.uploadFiles = uploadFiles;
    }

    @Override
    public PaginationResponse<EmployeeSeparations> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<EmployeeSeparations> employeeAssignmentHistoryPage = employeeSeparationsRepository.findAll(pageable);
        List<EmployeeSeparations> history = employeeAssignmentHistoryPage.getContent();

        PaginationResponse<EmployeeSeparations> response = new PaginationResponse<>();
        response.setData(history);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Employee Separations fetched successfully");

        Links links = Links.fromPage(employeeAssignmentHistoryPage, "/employee-separations");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeAssignmentHistoryPage, "/employee-separations");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeSeparations> getAll() {
        return employeeSeparationsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public EmployeeSeparations store(EmployeeSeparationsRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.employeeId()));

        User approver = userRepository.findById(request.currentApproverId())
                .orElseThrow(() -> new UsernameNotFoundException("Approver not found with id: " + request.currentApproverId()));

        // Create new EmployeeSeparations entity
        EmployeeSeparations separation = new EmployeeSeparations();
        separation.setEmployee(employee);
        separation.setSeparationType(request.separationType());
        separation.setSubmissionDate(request.submissionDate());
        separation.setRequestedLwd(request.requestedLwd());
        separation.setReason(request.reason());
        separation.setResignationLetterPath(request.resignationLetterPath());
        separation.setCurrentApprover(approver);
        separation.setCurrentApprovalLevel(request.currentApprovalLevel());
        separation.setSeparationsStatus(request.separationsStatus());
        separation.setNoticePeriodDays(request.noticePeriodDays());
        separation.setNoticeWaived(request.noticeWaived());
        separation.setNoticeWaiverReason(request.noticeWaiverReason());
        separation.setNoticeBuyoutAmount(request.noticeBuyoutAmount());
        separation.setIsBuyoutRecovered(request.isBuyoutRecovered());

        return employeeSeparationsRepository.save(separation);
    }

    @Override
    public EmployeeSeparations show(Long id) {
        return employeeSeparationsRepository.findById(id)
                .orElseThrow(() -> new EmployeeSeparationsNotFound("Employee Separations not found with id: " + id));
    }

    @Override
    public EmployeeSeparations update(Long id, EmployeeSeparationsRequest request) {
     return  null;
    }

    @Override
    public void destroy(Long id) {
        EmployeeSeparations history = employeeSeparationsRepository.findById(id)
                .orElseThrow(() -> new EmployeeSeparationsNotFound("Employee Separations not found with id: " + id));
        employeeSeparationsRepository.delete(history);
    }

    @Override
    public EmployeeSeparations saveSeparation(EmployeeSeparationsRequest request, List<MultipartFile> files) {

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.employeeId()));

        User approver = userRepository.findById(request.currentApproverId())
                .orElseThrow(() -> new UsernameNotFoundException("Approver not found with id: " + request.currentApproverId()));

        EmployeeSeparations separation = new EmployeeSeparations();
        separation.setEmployee(employee);
        separation.setSeparationType(request.separationType());
        separation.setSubmissionDate(request.submissionDate());
        separation.setRequestedLwd(request.requestedLwd());
        separation.setReason(request.reason());
        separation.setCurrentApprover(approver);
        separation.setCurrentApprovalLevel(request.currentApprovalLevel());
        separation.setSeparationsStatus(request.separationsStatus());
        separation.setNoticePeriodDays(request.noticePeriodDays());
        separation.setNoticeWaived(request.noticeWaived());
        separation.setNoticeWaiverReason(request.noticeWaiverReason());
        separation.setNoticeBuyoutAmount(request.noticeBuyoutAmount());
        separation.setIsBuyoutRecovered(request.isBuyoutRecovered());

        EmployeeSeparations saved = employeeSeparationsRepository.save(separation);

        if (files != null && !files.isEmpty()) {
            String folder = "employee-separations/" + saved.getId();
            StringBuilder urls = new StringBuilder();

            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    // Append uploaded file URL
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null; // No DB update inside lambda
                });
            }

            saved.setResignationLetterPath(urls.toString());
            saved = employeeSeparationsRepository.save(saved);
        }

        return saved;
    }

    @Override
    public List<EmployeeSeparations> getByStatus(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status parameter is required");
        }

        SeparationStatus enumStatus;
        try {
            // Convert incoming string to lowercase to match your enum
            enumStatus = SeparationStatus.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid separation status: " + status);
        }

        return employeeSeparationsRepository.findBySeparationsStatus(enumStatus);
    }

    @Override
    public EmployeeSeparations updateSeparation(Long id, EmployeeSeparationsRequest request, List<MultipartFile> files) {
        EmployeeSeparations separation = employeeSeparationsRepository.findById(id)
                .orElseThrow(() -> new EmployeeSeparationsNotFound("Employee Separation not found with id: " + id));

        // Update basic fields
        separation.setSeparationType(request.separationType() != null ? request.separationType() : separation.getSeparationType());
        separation.setSubmissionDate(request.submissionDate() != null ? request.submissionDate() : separation.getSubmissionDate());
        separation.setRequestedLwd(request.requestedLwd() != null ? request.requestedLwd() : separation.getRequestedLwd());
        separation.setReason(request.reason() != null ? request.reason() : separation.getReason());
        separation.setCurrentApprovalLevel(request.currentApprovalLevel() != null ? request.currentApprovalLevel() : separation.getCurrentApprovalLevel());
        separation.setSeparationsStatus(request.separationsStatus() != null ? request.separationsStatus() : separation.getSeparationsStatus());
        separation.setNoticePeriodDays(request.noticePeriodDays() != null ? request.noticePeriodDays() : separation.getNoticePeriodDays());
        separation.setNoticeWaived(request.noticeWaived() != null ? request.noticeWaived() : separation.getNoticeWaived());
        separation.setNoticeWaiverReason(request.noticeWaiverReason() != null ? request.noticeWaiverReason() : separation.getNoticeWaiverReason());
        separation.setNoticeBuyoutAmount(request.noticeBuyoutAmount() != null ? request.noticeBuyoutAmount() : separation.getNoticeBuyoutAmount());
        separation.setIsBuyoutRecovered(request.isBuyoutRecovered() != null ? request.isBuyoutRecovered() : separation.getIsBuyoutRecovered());

        if (files != null && !files.isEmpty()) {
            String folder = "employee-separations/" + separation.getId();
            StringBuilder urls = new StringBuilder();

            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null;
                });
            }

            if (separation.getResignationLetterPath() != null && !separation.getResignationLetterPath().isEmpty()) {
                urls.insert(0, separation.getResignationLetterPath() + ",");
            }
            separation.setResignationLetterPath(urls.toString());
        }

//        separation.setLastModifiedDate(LocalDateTime.now());

        return employeeSeparationsRepository.save(separation);
    }

}
