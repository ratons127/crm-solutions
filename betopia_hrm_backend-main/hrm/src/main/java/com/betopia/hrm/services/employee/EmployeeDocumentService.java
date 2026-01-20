package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.EmployeeDocumentDTO;
import com.betopia.hrm.domain.employee.entity.EmployeeDocument;
import com.betopia.hrm.domain.employee.request.EmployeeDocumentRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeDocumentService {

    PaginationResponse<EmployeeDocument> index(Sort.Direction direction, int page, int perPage);

    List<EmployeeDocumentDTO> getAllEmployeeDocuments();

    EmployeeDocumentDTO store(EmployeeDocumentRequest request, MultipartFile file);

    EmployeeDocumentDTO show(Long employeeDocumentId);

    EmployeeDocumentDTO update(Long employeeDocumentId, EmployeeDocumentRequest request,MultipartFile file);

    void delete(Long employeeDocumentId);


}
