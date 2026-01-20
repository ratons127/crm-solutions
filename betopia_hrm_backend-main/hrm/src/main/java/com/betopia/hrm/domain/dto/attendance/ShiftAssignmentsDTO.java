package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.attendance.enums.AssignmentSource;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ShiftAssignmentsDTO {

    private Long id;
    private EmployeeDTO employee;
    private ShiftDTO shift;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Boolean status;
    private AssignmentSource assignmentSource;
    private Long assignedBy;
    private LocalDateTime assignedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public ShiftDTO getShift() {
        return shift;
    }

    public void setShift(ShiftDTO shift) {
        this.shift = shift;
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

    public AssignmentSource getAssignmentSource() {
        return assignmentSource;
    }

    public void setAssignmentSource(AssignmentSource assignmentSource) {
        this.assignmentSource = assignmentSource;
    }

    public Long getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}
