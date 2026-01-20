package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.user.UserDTO;
import com.betopia.hrm.domain.employee.enums.ClearanceStatus;
import com.betopia.hrm.domain.employee.enums.Departments;

import java.time.LocalDateTime;

public class EmployeeClearanceChecklistDTO {

    private Long id;
    private EmployeeSeparationsDTO separation;
    private ExitClearanceItemDTO clearanceItem;
    private Departments department;
    private UserDTO assignedTo;
    private ClearanceStatus clearanceStatus;
    private String remarks;
    private String evidencePath;
    private LocalDateTime clearedDate;
    private UserDTO clearedBy;
    private LocalDateTime slaDeadline;
    private Boolean isOverdue;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeSeparationsDTO getSeparation() {
        return separation;
    }

    public void setSeparation(EmployeeSeparationsDTO separation) {
        this.separation = separation;
    }

    public ExitClearanceItemDTO getClearanceItem() {
        return clearanceItem;
    }

    public void setClearanceItem(ExitClearanceItemDTO clearanceItem) {
        this.clearanceItem = clearanceItem;
    }

    public Departments getDepartment() {
        return department;
    }

    public void setDepartment(Departments department) {
        this.department = department;
    }

    public UserDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    public ClearanceStatus getClearanceStatus() {
        return clearanceStatus;
    }

    public void setClearanceStatus(ClearanceStatus clearanceStatus) {
        this.clearanceStatus = clearanceStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEvidencePath() {
        return evidencePath;
    }

    public void setEvidencePath(String evidencePath) {
        this.evidencePath = evidencePath;
    }

    public LocalDateTime getClearedDate() {
        return clearedDate;
    }

    public void setClearedDate(LocalDateTime clearedDate) {
        this.clearedDate = clearedDate;
    }

    public UserDTO getClearedBy() {
        return clearedBy;
    }

    public void setClearedBy(UserDTO clearedBy) {
        this.clearedBy = clearedBy;
    }

    public LocalDateTime getSlaDeadline() {
        return slaDeadline;
    }

    public void setSlaDeadline(LocalDateTime slaDeadline) {
        this.slaDeadline = slaDeadline;
    }

    public Boolean getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }
}
