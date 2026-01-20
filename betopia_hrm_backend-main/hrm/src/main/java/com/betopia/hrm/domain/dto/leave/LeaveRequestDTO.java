package com.betopia.hrm.domain.dto.leave;

import com.betopia.hrm.domain.leave.entity.LeaveRequestDocument;
import com.betopia.hrm.domain.leave.enums.LeaveStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class LeaveRequestDTO {
    private Long id;
    private Long employeeId;
    private LeaveTypeDTO leaveType;
    private LeaveGroupAssignDTO leaveGroupAssign;
    private Boolean halfDay = false; // true = half day, false = full day
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal daysRequested;
    private String reason;
    private List<LeaveRequestDocumentDTO> proofDocumentPath = new ArrayList<>();
    private String justification; // optional, only used when overlaps or exceptions
    private LeaveStatus status ;
    private LocalDateTime requestedAt;
    private Long approvedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime deletedAt;
    private Long coveringEmployeeId;
    private Long createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LeaveTypeDTO getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveTypeDTO leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveGroupAssignDTO getLeaveGroupAssign() {
        return leaveGroupAssign;
    }

    public void setLeaveGroupAssign(LeaveGroupAssignDTO leaveGroupAssign) {
        this.leaveGroupAssign = leaveGroupAssign;
    }

    public Boolean getHalfDay() {
        return halfDay;
    }

    public void setHalfDay(Boolean halfDay) {
        this.halfDay = halfDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDaysRequested() {
        return daysRequested;
    }

    public void setDaysRequested(BigDecimal daysRequested) {
        this.daysRequested = daysRequested;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<LeaveRequestDocumentDTO> getProofDocumentPath() {
        return proofDocumentPath;
    }

    public void setProofDocumentPath(List<LeaveRequestDocumentDTO> proofDocumentPath) {
        this.proofDocumentPath = proofDocumentPath;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getCoveringEmployeeId() {
        return coveringEmployeeId;
    }

    public void setCoveringEmployeeId(Long coveringEmployeeId) {
        this.coveringEmployeeId = coveringEmployeeId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
