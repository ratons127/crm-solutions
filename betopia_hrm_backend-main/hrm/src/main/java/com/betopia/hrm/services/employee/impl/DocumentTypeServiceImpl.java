package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.DocumentType;
import com.betopia.hrm.domain.employee.repository.DocumentTypeRepository;
import com.betopia.hrm.domain.employee.request.DocumentTypeRequest;
import com.betopia.hrm.services.employee.DocumentTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private final DocumentTypeRepository documentTypeRepository;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository) {
        this.documentTypeRepository = documentTypeRepository;
    }

    @Override
    public PaginationResponse<DocumentType> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<DocumentType> documentTypePage = documentTypeRepository.findAll(pageable);
        List<DocumentType> documentTypes = documentTypePage.getContent();
        PaginationResponse<DocumentType> response = new PaginationResponse<>();
        response.setData(documentTypes);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Document Types fetch successful");
        Links links = Links.fromPage(documentTypePage, "/documentTypePages");
        response.setLinks(links);
        Meta meta = Meta.fromPage(documentTypePage, "/documentTypePages");
        response.setMeta(meta);
        return response;
    }

    @Override
    public List<DocumentType> getAllDocumentTypes() {
        return documentTypeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public DocumentType create(DocumentTypeRequest request) {
        DocumentType documentType = new DocumentType();

        documentType.setName(request.name());
        documentType.setDescription(request.description());
        documentType.setCategory(request.category());
        documentType.setRequired(request.isRequired());
        documentType.setTimeBound(request.isTimeBound());
        documentType.setDefaultValidityMonths(request.defaultValidityMonths());

        return documentTypeRepository.save(documentType);
    }

    @Override
    public DocumentType update(Long documentTypeId, DocumentTypeRequest request) {
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new RuntimeException("Document Type not found with id: " + documentTypeId));

        documentType.setName(request.name() != null ? request.name() : documentType.getName());
        documentType.setDescription(request.description() != null ? request.description() : documentType.getDescription());
        documentType.setCategory(request.category() != null ? request.category() : documentType.getCategory());
        documentType.setRequired(request.isRequired());
        documentType.setTimeBound(request.isTimeBound());
        documentType.setDefaultValidityMonths(request.defaultValidityMonths());

        documentTypeRepository.save(documentType);
        return documentType;
    }

    @Override
    public DocumentType show(Long documentTypeId) {
        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
                .orElseThrow(() -> new RuntimeException("Document Type not found with id: " + documentTypeId));
        return documentType;
    }

    @Override
    public void delete(Long documentTypeId) {
        documentTypeRepository.deleteById(documentTypeId);
    }
}
