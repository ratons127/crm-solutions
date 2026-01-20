package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.domain.dto.company.BusinessUnitDTO;
import com.betopia.hrm.domain.dto.company.CompanyDTO;
import com.betopia.hrm.domain.dto.company.DepartmentDTO;
import com.betopia.hrm.domain.dto.company.TeamDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceGroupDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class EmployeeShiftAssignDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Long employeeSerialId;
    private ShiftDTO shift;
    private CompanyDTO company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getEmployeeSerialId() {
        return employeeSerialId;
    }

    public void setEmployeeSerialId(Long employeeSerialId) {
        this.employeeSerialId = employeeSerialId;
    }

    public ShiftDTO getShift() {
        return shift;
    }

    public void setShift(ShiftDTO shift) {
        this.shift = shift;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }
}
