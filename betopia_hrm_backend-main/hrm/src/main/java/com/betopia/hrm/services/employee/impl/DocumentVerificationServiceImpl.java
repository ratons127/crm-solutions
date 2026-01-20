package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.*;
import com.betopia.hrm.domain.employee.enums.DocumentVerificationStatus;
import com.betopia.hrm.domain.employee.exception.employeedocumentstatus.EmployeeDocumentStatusNotFoundException;
import com.betopia.hrm.domain.employee.exception.employeedocumentstatus.EmployeeDocumentVersionNotFoundException;
import com.betopia.hrm.domain.employee.repository.DocumentVerificationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeDocumentRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeDocumentVersionRepository;
import com.betopia.hrm.domain.employee.request.DocumentVerificationRequest;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.DocumentVerificationService;
import com.betopia.hrm.webapp.util.NotificationSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentVerificationServiceImpl implements DocumentVerificationService {


    private final DocumentVerificationRepository documentVerificationRepository;
    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final EmployeeDocumentVersionRepository employeeDocumentVersionRepository;
    private final UserRepository userRepository;

    public DocumentVerificationServiceImpl(DocumentVerificationRepository documentVerificationRepository,
                                           EmployeeDocumentRepository employeeDocumentRepository,
                                           EmployeeDocumentVersionRepository employeeDocumentVersionRepository,
                                           UserRepository userRepository) {
        this.documentVerificationRepository = documentVerificationRepository;
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.employeeDocumentVersionRepository = employeeDocumentVersionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<DocumentVerification> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<DocumentVerification> documentVerificationPage = documentVerificationRepository.findAll(pageable);
        List<DocumentVerification> documentVerifications = documentVerificationPage.getContent();
        PaginationResponse<DocumentVerification> response = new PaginationResponse<>();

        response.setData(documentVerifications);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All verified documents fetch successful");

        Links links = Links.fromPage(documentVerificationPage, "/document-verifications");
        response.setLinks(links);
        Meta meta = Meta.fromPage(documentVerificationPage, "/document-verifications");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<DocumentVerification> getAllDocumentVerifications() {
        return documentVerificationRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public DocumentVerification show(Long documentVerificationId) {
        DocumentVerification documentVerification = documentVerificationRepository.findById(documentVerificationId)
                .orElseThrow(() -> new RuntimeException("document not found with id: " + documentVerificationId));

        return documentVerification;
    }

    @Override
    public DocumentVerification store(DocumentVerificationRequest request) {

        EmployeeDocument employeeDocument = employeeDocumentRepository.findById(request.employeeDocumentId())
                .orElseThrow(() -> new EmployeeDocumentStatusNotFoundException("Employee document not found: " + request.employeeDocumentId()));

        EmployeeDocumentVersion employeeDocumentVersion = employeeDocumentVersionRepository.findById(request.employeeDocumentVersionId())
                .orElseThrow(() -> new EmployeeDocumentVersionNotFoundException("Employee Document version not found: " + request.employeeDocumentVersionId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.userId()));


        DocumentVerification documentVerification = new DocumentVerification();

        documentVerification.setEmployeeDocument(employeeDocument);
        documentVerification.setEmployeeDocumentVersion(employeeDocumentVersion);
        documentVerification.setUser(user);
        documentVerification.setVerifiedAt(LocalDateTime.now());
        documentVerification.setRemarks(request.remarks());
        documentVerification.setStatus(request.status() == null ? DocumentVerificationStatus.PENDING : request.status());

        return documentVerificationRepository.save(documentVerification);
    }

    @Override
    public DocumentVerification update(Long documentVerificationId, DocumentVerificationRequest request) {

        DocumentVerification documentVerification = documentVerificationRepository.findById(documentVerificationId)
                .orElseThrow(() -> new EmployeeDocumentStatusNotFoundException("document not found with id: " + documentVerificationId));

        EmployeeDocument employeeDocument = employeeDocumentRepository.findById(request.employeeDocumentId())
                .orElseThrow(() -> new EmployeeDocumentStatusNotFoundException("Employee document not found: " + request.employeeDocumentId()));

        EmployeeDocumentVersion employeeDocumentVersion = employeeDocumentVersionRepository.findById(request.employeeDocumentVersionId())
                .orElseThrow(() -> new EmployeeDocumentVersionNotFoundException("Employee Document version not found: " + request.employeeDocumentVersionId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.userId()));

        documentVerification.setEmployeeDocument(employeeDocument != null ? employeeDocument : documentVerification.getEmployeeDocument());
        documentVerification.setEmployeeDocumentVersion(employeeDocumentVersion != null ? employeeDocumentVersion : documentVerification.getEmployeeDocumentVersion());
        documentVerification.setUser(user != null ? user : documentVerification.getUser());
        documentVerification.setRemarks(request.remarks() != null ? request.remarks() : documentVerification.getRemarks());
        documentVerification.setVerifiedAt(request.verifiedAt() != null ? request.verifiedAt() : documentVerification.getVerifiedAt());
        documentVerification.setStatus(request.status() != null ? request.status() : documentVerification.getStatus());

        return documentVerificationRepository.save(documentVerification);
    }

    @Override
    public void destroy(Long documentVerificationId) {
        documentVerificationRepository.deleteById(documentVerificationId);
    }

    @Override
    public DocumentVerification updateStatus(Long documentVerificationId, DocumentVerificationRequest request) {
        DocumentVerification documentVerification = documentVerificationRepository.findById(documentVerificationId)
                .orElseThrow(() -> new RuntimeException("DocumentVerification not found with id: " + documentVerificationId));

        DocumentVerificationStatus previousStatus = documentVerification.getStatus();
        DocumentVerificationStatus newStatus = request.status();

        if (newStatus == null) {
            throw new RuntimeException("New status cannot be null");
        }

        documentVerification.setStatus(newStatus);
        documentVerification.setRemarks(request.remarks());
        documentVerification.setVerifiedAt(LocalDateTime.now());

        DocumentVerification saved = documentVerificationRepository.save(documentVerification);

        if (!newStatus.equals(previousStatus) &&
                (newStatus == DocumentVerificationStatus.VERIFIED || newStatus == DocumentVerificationStatus.REJECTED)) {
            sendNotification(saved.getId());
        }

        return saved;
    }

    @Override
    public void sendNotification(Long documentVerificationId) {

//     Future scope

    }

}
