package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.DocumentVerification;
import com.betopia.hrm.domain.employee.request.DocumentVerificationRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DocumentVerificationService {

    PaginationResponse<DocumentVerification> index(Sort.Direction direction, int page, int perPage);

    List<DocumentVerification> getAllDocumentVerifications();

    DocumentVerification store(DocumentVerificationRequest request);

    DocumentVerification show(Long documentVerificationId);

    DocumentVerification update(Long documentVerificationId, DocumentVerificationRequest request);

    void destroy(Long documentVerificationId);

    DocumentVerification updateStatus(Long documentVerificationId, DocumentVerificationRequest request);

    void sendNotification(Long documentVerificationId);


}
