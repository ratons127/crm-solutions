package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.DocumentExpiryAlert;
import com.betopia.hrm.domain.employee.request.DocumentExpiryAlertRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DocumentExpiryAlertService {

    PaginationResponse<DocumentExpiryAlert> index(Sort.Direction direction, int page, int perPage);

    List<DocumentExpiryAlert> getAllDocumentExpiryAlerts();

    DocumentExpiryAlert store(DocumentExpiryAlertRequest request);

    DocumentExpiryAlert show(Long documentExpiryAlertId);

    DocumentExpiryAlert update(Long documentExpiryAlertId, DocumentExpiryAlertRequest request);

    void destroy(Long documentExpiryAlertId);

}
