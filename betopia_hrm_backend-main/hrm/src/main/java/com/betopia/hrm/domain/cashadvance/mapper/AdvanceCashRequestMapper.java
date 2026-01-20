package com.betopia.hrm.domain.cashadvance.mapper;

import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashRequestRequest;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.exception.DepartmentNotFoundException;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.DesignationRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeTypeRepository;
import org.springframework.stereotype.Component;

@Component
public class AdvanceCashRequestMapper {

    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;

    public AdvanceCashRequestMapper(EmployeeTypeRepository employeeTypeRepository, EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, DesignationRepository designationRepository) {
        this.employeeTypeRepository = employeeTypeRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.designationRepository = designationRepository;
    }


    /**
     * Map CreateRequest DTO -> Entity
     */
    public AdvanceCashRequest toEntity(AdvanceCashRequestRequest request) {
        AdvanceCashRequest advanceCashRequest = new AdvanceCashRequest();
        entityDto(advanceCashRequest, request);
        return advanceCashRequest;
    }

    /**
     * Update Entity fields from CreateRequest
     */
    public void entityDto(AdvanceCashRequest advanceCashRequest, AdvanceCashRequestRequest request) {

       // advanceCashRequest.setRequestDate(request.requestDate());
        advanceCashRequest.setRequestedAmount(request.requestedAmount());
        advanceCashRequest.setServiceChargeAmount(request.serviceChargeAmount());
        advanceCashRequest.setDeductedAmount(request.deductedAmount());
        advanceCashRequest.setReason(request.reason());
    }
}
