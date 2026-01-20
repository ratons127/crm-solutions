package com.betopia.hrm.services.employee.separationdocuments.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationDocumentsDTO;
import com.betopia.hrm.domain.dto.employee.mapper.SeparationDocumentsMapper;
import com.betopia.hrm.domain.employee.entity.SeparationDocuments;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.SeparationDocumentsRepository;
import com.betopia.hrm.domain.employee.request.SeparationDocumentsRequest;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.separationdocuments.SeparationDocumentsService;
import com.betopia.hrm.webapp.util.UploadFiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SeparationDocumentsServiceImpl implements SeparationDocumentsService {

    private final SeparationDocumentsRepository separationDocumentsRepository;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final UserRepository userRepository;
    private final SeparationDocumentsMapper separationDocumentsMapper;
    private final UploadFiles uploadFiles;

    public SeparationDocumentsServiceImpl(SeparationDocumentsRepository separationDocumentsRepository,
                                          EmployeeSeparationsRepository employeeSeparationsRepository,
                                          UserRepository userRepository,SeparationDocumentsMapper separationDocumentsMapper,
                                          UploadFiles uploadFiles) {

        this.separationDocumentsRepository = separationDocumentsRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.userRepository = userRepository;
        this.separationDocumentsMapper = separationDocumentsMapper;
        this.uploadFiles = uploadFiles;
    }

    @Override
    public PaginationResponse<SeparationDocumentsDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<SeparationDocuments> documentsPage = separationDocumentsRepository.findAll(pageable);

        // Get content from page
        List<SeparationDocuments> separationDocuments = documentsPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<SeparationDocumentsDTO> separationDocumentDTOS = separationDocumentsMapper.toDTOList(separationDocuments);

        // Create pagination response
        PaginationResponse<SeparationDocumentsDTO> response = new PaginationResponse<>();
        response.setData(separationDocumentDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All separation document successfully");

        // Set links
        Links links = Links.fromPage(documentsPage, "/seperation-doc");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(documentsPage, "/seperation-doc");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<SeparationDocumentsDTO> getAll() {
        List<SeparationDocuments> documents = separationDocumentsRepository.findAll();
        return separationDocumentsMapper.toDTOList(documents);
    }

    @Override
    public SeparationDocumentsDTO show(Long id) {
        SeparationDocuments documents = separationDocumentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Separation documents not found with id: " + id));
        return separationDocumentsMapper.toDTO(documents);
    }

    @Override
    public SeparationDocumentsDTO update(Long id, SeparationDocumentsRequest request, List<MultipartFile> files) {
        SeparationDocuments documents = new SeparationDocuments();

        documents.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        documents.setUploadedBy(userRepository.findById(request.uploadedById())
                .orElseThrow(() -> new RuntimeException("User not found")));

        documents.setDocumentType(request.documentType());
        documents.setDocumentName(request.documentName());
        documents.setMimeType(request.mimeType());
        documents.setGeneratedBySystem(request.generatedBySystem());
        documents.setIsSigned(request.isSigned());
        documents.setSignedDate(request.signedDate());
        documents.setIsEmployeeAccessible(request.isEmployeeAccessible());


        if (files != null && !files.isEmpty()) {
            String folder = "seperation-doc/" + documents.getId();
            StringBuilder urls = new StringBuilder();

            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null;
                });
            }

            if (documents.getFilePath() != null && !documents.getFilePath().isEmpty()) {
                urls.insert(0, documents.getFilePath() + ",");
            }
            documents.setFilePath(urls.toString());
        }

        SeparationDocuments saved = separationDocumentsRepository.save(documents);
        return separationDocumentsMapper.toDTO(saved);
    }


    @Override
    public SeparationDocumentsDTO saveSeparation(SeparationDocumentsRequest request, List<MultipartFile> files) {
        SeparationDocuments documents = new SeparationDocuments();

        documents.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee separation not found with id: " + request.separationId())));

        documents.setUploadedBy(userRepository.findById(request.uploadedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.uploadedById())));


        documents.setDocumentType(request.documentType());
        documents.setDocumentName(request.documentName());
        documents.setMimeType(request.mimeType());
        documents.setGeneratedBySystem(request.generatedBySystem());
        documents.setIsSigned(request.isSigned());
        documents.setSignedDate(request.signedDate());
        documents.setIsEmployeeAccessible(request.isEmployeeAccessible());

        StringBuilder urls = new StringBuilder();
        if (files != null && !files.isEmpty()) {
            String folder = "separation-documents/tmp";
            for (MultipartFile file : files) {
                uploadFiles.upload(file, folder, (fileUrl) -> {
                    if (urls.length() > 0) urls.append(",");
                    urls.append(fileUrl);
                    return null;
                });
            }
        }

        documents.setFilePath(urls.toString());

        SeparationDocuments saved = separationDocumentsRepository.save(documents);

        return separationDocumentsMapper.toDTO(saved);
    }

    @Override
    public void destroy(Long id) {
        SeparationDocuments documents = separationDocumentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Separation documents not not found"));

        separationDocumentsRepository.delete(documents);
    }

}
