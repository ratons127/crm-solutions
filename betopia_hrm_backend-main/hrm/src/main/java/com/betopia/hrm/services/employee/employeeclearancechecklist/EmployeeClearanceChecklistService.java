package com.betopia.hrm.services.employee.employeeclearancechecklist;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.EmployeeClearanceChecklistDTO;
import com.betopia.hrm.domain.employee.request.EmployeeClearanceChecklistRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeClearanceChecklistService {

    PaginationResponse<EmployeeClearanceChecklistDTO> index(Sort.Direction direction, int page, int perPage);

    List<EmployeeClearanceChecklistDTO> getAll();

    EmployeeClearanceChecklistDTO store(EmployeeClearanceChecklistRequest request);

    EmployeeClearanceChecklistDTO show(Long id);

    EmployeeClearanceChecklistDTO updateChecklist(Long id,  EmployeeClearanceChecklistRequest request,List<MultipartFile> files);

    void destroy(Long id);

    EmployeeClearanceChecklistDTO saveChecklist(EmployeeClearanceChecklistRequest request, List<MultipartFile> files);

}
