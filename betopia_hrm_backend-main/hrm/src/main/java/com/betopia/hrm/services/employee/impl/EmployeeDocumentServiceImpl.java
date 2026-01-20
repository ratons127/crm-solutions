package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.EmployeeDocumentDTO;
import com.betopia.hrm.domain.dto.employee.mapper.EmployeeDocumentMapper;
import com.betopia.hrm.domain.employee.entity.*;
import com.betopia.hrm.domain.employee.exception.employeedocumentstatus.EmployeeDocumentStatusNotFoundException;
import com.betopia.hrm.domain.employee.repository.DocumentTypeRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeDocumentRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeDocumentVersionRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeDocumentRequest;
import com.betopia.hrm.domain.employee.request.EmployeeDocumentVersionRequest;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.EmployeeDocumentService;
import com.betopia.hrm.webapp.util.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class EmployeeDocumentServiceImpl implements EmployeeDocumentService {

    private final EmployeeDocumentRepository employeeDocumentRepository;
    private final EmployeeDocumentVersionRepository employeeDocumentVersionRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final EmployeeDocumentMapper employeeDocumentMapper;

    public EmployeeDocumentServiceImpl(EmployeeDocumentRepository employeeDocumentRepository,
                                       EmployeeDocumentVersionRepository employeeDocumentVersionRepository,
                                       DocumentTypeRepository documentTypeRepository,
                                       EmployeeRepository employeeRepository, S3Service s3Service,
                                       UserRepository userRepository, EmployeeDocumentMapper employeeDocumentMapper) {
        this.employeeDocumentRepository = employeeDocumentRepository;
        this.employeeDocumentVersionRepository = employeeDocumentVersionRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.employeeRepository = employeeRepository;
        this.s3Service = s3Service;

        this.userRepository = userRepository;
        this.employeeDocumentMapper = employeeDocumentMapper;
    }

    @Override
    public PaginationResponse<EmployeeDocument> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<EmployeeDocument> employeeDocumentPage = employeeDocumentRepository.findAll(pageable);

        List<EmployeeDocument> employeeDocumentPages = employeeDocumentPage.getContent();

        PaginationResponse<EmployeeDocument> response = new PaginationResponse<>();

        response.setData(employeeDocumentPages);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All employee documents fetch successful");

        Links links = Links.fromPage(employeeDocumentPage, "/employee-document-pages");
        response.setLinks(links);

        Meta meta = Meta.fromPage(employeeDocumentPage, "/employeeDocumentPages");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<EmployeeDocumentDTO> getAllEmployeeDocuments() {
        return employeeDocumentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(employeeDocumentMapper::toDTO)
                .toList();
    }


    @Override
    public EmployeeDocumentDTO show(Long employeeDocumentId) {

        EmployeeDocument employeeDocument = employeeDocumentRepository.findById(employeeDocumentId)
                .orElseThrow(() -> new EmployeeDocumentStatusNotFoundException("employee document not found with id: " + employeeDocumentId));

        return employeeDocumentMapper.toDTO(employeeDocument);
    }

    @Override
    public EmployeeDocumentDTO store(EmployeeDocumentRequest request, MultipartFile file) {

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + request.employeeId()));

        DocumentType documentType = documentTypeRepository.findById(request.documentTypeId())
                .orElseThrow(() -> new RuntimeException("DocumentType not found: " + request.documentTypeId()));

        EmployeeDocument employeeDocument = new EmployeeDocument();
        employeeDocument.setEmployee(employee);
        employeeDocument.setDocumentType(documentType);
        employeeDocument.setIssueDate(request.issueDate());
        employeeDocument.setExpiryDate(request.expiryDate());
        employeeDocument.setStatus(request.status());
        employeeDocument.setDocumentStatus(request.documentStatus());

        if (file != null && !file.isEmpty()) {
            createDocument(file, employeeDocument);
        }

        if (request.employeeDocumentVersions() != null && !request.employeeDocumentVersions().isEmpty()) {

            EmployeeDocumentVersionRequest versionRequest = request.employeeDocumentVersions().get(0);

            if (versionRequest.userId() == null) {
                throw new RuntimeException("userId is required");
            }

            User user = userRepository.findById(versionRequest.userId())
                    .orElseThrow(() -> new RuntimeException("User not found : " + versionRequest.userId()));

            EmployeeDocumentVersion documentVersion = new EmployeeDocumentVersion();
            documentVersion.setVersionNo(1);
            documentVersion.setUploadedAt(LocalDateTime.now());
            documentVersion.setCurrent(true);
            documentVersion.setFilePath(employeeDocument.getFilePath());
            documentVersion.setUser(user);
            documentVersion.setRemarks(versionRequest.remarks());

            employeeDocument.addDetail(documentVersion);
            employeeDocument.setCurrentVersion(documentVersion);
        }
        return employeeDocumentMapper.toDTO(employeeDocumentRepository.save(employeeDocument));
    }

    @Override
    public EmployeeDocumentDTO update(Long documentId, EmployeeDocumentRequest request, MultipartFile file) {

        EmployeeDocument employeeDocument = employeeDocumentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("EmployeeDocument not found: " + documentId));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found: " + request.employeeId()));

        DocumentType documentType = documentTypeRepository.findById(request.documentTypeId())
                .orElseThrow(() -> new RuntimeException("DocumentType not found: " + request.documentTypeId()));

        employeeDocument.setEmployee(request.employeeId() != null ? employeeRepository.findById(request.employeeId()) .orElse(null) : employeeDocument.getEmployee());
        employeeDocument.setDocumentType(request.documentTypeId() != null ? documentTypeRepository.findById(request.documentTypeId()).orElse(null) : employeeDocument.getDocumentType());
        employeeDocument.setIssueDate(request.issueDate() != null ? request.issueDate() : employeeDocument.getIssueDate());
        employeeDocument.setExpiryDate(request.expiryDate() != null ? request.expiryDate() : employeeDocument.getExpiryDate());
        employeeDocument.setStatus(request.status() != null ? request.status() : employeeDocument.getStatus());
        employeeDocument.setDocumentStatus(request.documentStatus() != null ? request.documentStatus() : employeeDocument.getDocumentStatus());

        if (file != null && !file.isEmpty()) {
            createDocument(file, employeeDocument);
        } else if (request.filePath() != null && !request.filePath().isBlank()) {
            employeeDocument.setFilePath(request.filePath());
        }

        if (request.employeeDocumentVersions() == null || request.employeeDocumentVersions().isEmpty()) {
            throw new RuntimeException("At least one version payload is required for update");
        }

        EmployeeDocumentVersionRequest versionRequest = request.employeeDocumentVersions().get(0);

        if (versionRequest.userId() == null) {
            throw new RuntimeException("userId is required");
        }

        User user = userRepository.findById(versionRequest.userId())
                .orElseThrow(() -> new RuntimeException("User not found : " + versionRequest.userId()));

        if (employeeDocument.getEmployeeDocumentVersions() == null) {
            employeeDocument.setEmployeeDocumentVersions(new java.util.ArrayList<>());
        }

        EmployeeDocumentVersion prevCurrent = employeeDocument.getEmployeeDocumentVersions()
                .stream()
                .filter(EmployeeDocumentVersion::isCurrent)
                .findFirst()
                .orElse(null);
        if (prevCurrent != null) {
            prevCurrent.setCurrent(false);
        }

        Integer nextVersionNo = employeeDocument.getEmployeeDocumentVersions()
                .stream()
                .map(v -> v.getVersionNo() == null ? 0 : v.getVersionNo())
                .max(java.util.Comparator.naturalOrder())
                .orElse(0) + 1;

        EmployeeDocumentVersion documentVersion = new EmployeeDocumentVersion();
        documentVersion.setVersionNo(nextVersionNo);
        documentVersion.setUploadedAt(java.time.LocalDateTime.now());
        documentVersion.setCurrent(true);
        documentVersion.setUser(user);
        documentVersion.setRemarks(versionRequest.remarks());

        String pathForThisVersion = employeeDocument.getFilePath();
        if ((pathForThisVersion == null || pathForThisVersion.isBlank()) && prevCurrent != null) {
            pathForThisVersion = prevCurrent.getFilePath();
        }
        documentVersion.setFilePath(pathForThisVersion);

        employeeDocument.addDetail(documentVersion);
        employeeDocument.setCurrentVersion(documentVersion);

        return employeeDocumentMapper.toDTO(employeeDocumentRepository.save(employeeDocument));
    }


    @Override
    public void delete(Long employeeDocumentId) {
        employeeDocumentRepository.deleteById(employeeDocumentId);
    }

    private void createDocument(MultipartFile file, EmployeeDocument employeeDocument) {
        try {
            // Convert MultipartFile → Temp File
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);

            // Build S3 key (use UUID to avoid name collisions)
            String imageKey = "employeeDocuments/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Upload to S3
            String imageUrl = s3Service.uploadFile(imageKey, convFile);

            // Set filePath in document and its first version
            employeeDocument.setFilePath(imageUrl);

            if (!employeeDocument.getEmployeeDocumentVersions().isEmpty()) {
                EmployeeDocumentVersion docVersion = employeeDocument.getEmployeeDocumentVersions().get(0);
                docVersion.setFilePath(imageUrl);
            }

        } catch (IOException e) {
            throw new RuntimeException("File saving failed: " + file.getOriginalFilename(), e);
        }
    }
}

