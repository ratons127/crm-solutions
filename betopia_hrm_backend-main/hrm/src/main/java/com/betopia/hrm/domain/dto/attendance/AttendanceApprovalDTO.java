package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.attendance.enums.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttendanceApprovalDTO {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long employeeSerialId;
    private Long manualAttendanceId;
    private LocalDate date;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private AttendanceStatus manualApprovalStatus;
    private AdjustmentType adjustmentType;
    private String reason;
    private Long approverId;
    private Source submittedBy;
    private LocalDateTime submittedAt;

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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getEmployeeSerialId() {
        return employeeSerialId;
    }

    public void setEmployeeSerialId(Long employeeSerialId) {
        this.employeeSerialId = employeeSerialId;
    }

    public Long getManualAttendanceId() {
        return manualAttendanceId;
    }

    public void setManualAttendanceId(Long manualAttendanceId) {
        this.manualAttendanceId = manualAttendanceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getInTime() {
        return inTime;
    }

    public void setInTime(LocalDateTime inTime) {
        this.inTime = inTime;
    }

    public LocalDateTime getOutTime() {
        return outTime;
    }

    public void setOutTime(LocalDateTime outTime) {
        this.outTime = outTime;
    }

    public AttendanceStatus getManualApprovalStatus() {
        return manualApprovalStatus;
    }

    public void setManualApprovalStatus(AttendanceStatus manualApprovalStatus) {
        this.manualApprovalStatus = manualApprovalStatus;
    }

    public AdjustmentType getAdjustmentType() {
        return adjustmentType;
    }

    public void setAdjustmentType(AdjustmentType adjustmentType) {
        this.adjustmentType = adjustmentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public Source getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(Source submittedBy) {
        this.submittedBy = submittedBy;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
