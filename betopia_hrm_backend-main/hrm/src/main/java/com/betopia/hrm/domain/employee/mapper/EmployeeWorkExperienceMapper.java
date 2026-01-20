package com.betopia.hrm.domain.employee.mapper;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeWorkExperience;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeWorkExperienceRequest;
import org.springframework.stereotype.Component;

@Component
public class EmployeeWorkExperienceMapper {

    private final EmployeeRepository employeeRepository;

    public EmployeeWorkExperienceMapper(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;

    }


    /**
     * Map CreateRequest DTO -> Entity
     */
    public EmployeeWorkExperience toEntity(EmployeeWorkExperienceRequest request) {
        EmployeeWorkExperience EmployeeWorkExperience = new EmployeeWorkExperience();
        entityDto(EmployeeWorkExperience, request);
        return EmployeeWorkExperience;
    }

    /**
     * Update Entity fields from CreateRequest
     */
    public void entityDto(EmployeeWorkExperience employeeWorkExperience, EmployeeWorkExperienceRequest request) {
        employeeWorkExperience.setCompanyName(request.companyName());
        employeeWorkExperience.setJobDescription(request.jobDescription());
        employeeWorkExperience.setJobTitle(request.jobTitle());
        employeeWorkExperience.setLocation(request.location());
        employeeWorkExperience.setFromDate(request.fromDate());
        employeeWorkExperience.setToDate(request.toDate());
        employeeWorkExperience.setTenure(request.tenure());
        employeeWorkExperience.setImage(request.image());
        employeeWorkExperience.setImageUrl(request.imageUrl());

        if(request.employeeId()!=null) {
            Employee employee = employeeRepository.findById(request.employeeId())
                    .orElseThrow(() -> new EmployeeNotFound("Employee not found: " + request.employeeId()));
            employeeWorkExperience.setEmployee(employee);
        }
        else
            employeeWorkExperience.setEmployee(null);


    }
}
