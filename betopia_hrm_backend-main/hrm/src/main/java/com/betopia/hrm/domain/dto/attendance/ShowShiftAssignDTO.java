package com.betopia.hrm.domain.dto.attendance;


import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.dto.employee.ShowAssignedEmployeeDTO;

import java.time.LocalDate;

public class ShowShiftAssignDTO {

    private Long id;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Boolean status;
    private ShowAssignedEmployeeDTO employee;
    private ShiftDTO shift;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ShiftDTO getShift() {
        return shift;
    }

    public void setShift(ShiftDTO shift) {
        this.shift = shift;
    }

    public ShowAssignedEmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(ShowAssignedEmployeeDTO employee) {
        this.employee = employee;
    }
}
