package com.betopia.hrm.services.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeWorkExperience;
import com.betopia.hrm.domain.employee.request.EmployeeWorkExperienceRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeWorkExperienceService {

    PaginationResponse<EmployeeWorkExperience> index(Sort.Direction direction, int page, int perPage);

    List<EmployeeWorkExperience> getAllEmployeeWorkExperience();

    EmployeeWorkExperience store(EmployeeWorkExperienceRequest request);

    EmployeeWorkExperience show(Long employeeWorkExperienceId);

    EmployeeWorkExperience update(Long employeeWorkExperienceId, EmployeeWorkExperienceRequest request);

    void delete(Long employeeWorkExperienceId);

    ResponseEntity<GlobalResponse> uploadFile(Long employeeWorkExperienceId, MultipartFile file);

    ResponseEntity<GlobalResponse> deleteFile(Long employeeWorkExperienceId);

}
