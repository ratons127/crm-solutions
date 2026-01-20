package com.betopia.hrm.domain.dto.leave;

import com.betopia.hrm.domain.dto.employee.EmployeeDTO;

import java.time.LocalDateTime;

public class ApprovalsShowDTO {

    private Long id;
    private LeaveRequestDTO leaveRequest;
    private EmployeeDTO employee;
    private Long approverId;
    private Integer level;
    private String leaveStatus;
    private String remarks;
    private LocalDateTime actionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveRequestDTO getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequestDTO leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }
}
