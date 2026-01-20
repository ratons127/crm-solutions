package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeShiftAssignDTO;
import com.betopia.hrm.domain.employee.request.EmployeeRequest;
import com.betopia.hrm.domain.employee.request.EmployeeShiftAssignRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    PaginationResponse<EmployeeDTO> index(Sort.Direction direction, int page, int perPage, String keyword);

    PaginationResponse<EmployeeDTO> searchEmployees(Long departmentId,
                                   Long designationId,
                                   Long workplaceId,
                                   Long workPlaceGroupId,
                                   Long companyId,
                                   Long gradeId,
                                   Long employeeTypeId,
                                   String employeeName,
                                   Pageable pageable);

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO store(EmployeeRequest request);

    EmployeeDTO show(Long employeeId);

    EmployeeDTO update(Long employeeId, EmployeeRequest request);

    void delete(Long employeeId);

    Long findReportingManagerId(Long employeeId);

    List<EmployeeShiftAssignDTO> getAllEmployeeForShiftAssignments(EmployeeShiftAssignRequest employee);

    Map<String,String> getSupervisorName();

    void importEmployees(MultipartFile file);

    ResponseEntity<GlobalResponse> uploadEmployeeImage(Long employeeId, MultipartFile file);

    ResponseEntity<GlobalResponse> deleteEmployeeImage(Long employeeId);
}
