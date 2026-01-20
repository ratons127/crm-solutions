package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;

import com.betopia.hrm.domain.employee.entity.DocumentType;
import com.betopia.hrm.domain.employee.request.DocumentTypeRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DocumentTypeService {

    PaginationResponse<DocumentType> index(Sort.Direction direction, int page, int perPage);

    List<DocumentType> getAllDocumentTypes();

    DocumentType create(DocumentTypeRequest request);

    DocumentType show(Long documentTypeId);

    DocumentType update(Long documentTypeId, DocumentTypeRequest request);

    void delete(Long documentTypeId);

}
