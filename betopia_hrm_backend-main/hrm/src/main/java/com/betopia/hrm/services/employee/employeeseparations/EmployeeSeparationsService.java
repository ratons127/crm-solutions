package com.betopia.hrm.services.employee.employeeseparations;

import com.betopia.hrm.domain.employee.entity.EmployeeSeparations;
import com.betopia.hrm.domain.employee.request.EmployeeSeparationsRequest;
import com.betopia.hrm.services.base.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeSeparationsService extends BaseService<EmployeeSeparations, EmployeeSeparationsRequest,Long> {


    EmployeeSeparations saveSeparation(EmployeeSeparationsRequest request, List<MultipartFile> files);
    List<EmployeeSeparations> getByStatus(String status);

    EmployeeSeparations updateSeparation(Long id, EmployeeSeparationsRequest request, List<MultipartFile> files);
}
