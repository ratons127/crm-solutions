package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.attendance.enums.*;
import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "attendance_approvals")
public class AttendanceApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "employee_name", nullable = false, length = 100)
    private String employeeName;

    @Column(name = "employee_serial_id",unique = true)
    private Long employeeSerialId;

    @Column(name = "manual_attendance_id")
    private Long manualAttendanceId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "in_time")
    private LocalDateTime inTime;

    @Column(name = "out_time")
    private LocalDateTime outTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "manual_approval_status", nullable = false)
    private AttendanceStatus manualApprovalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type", length = 15)
    private AdjustmentType adjustmentType;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "submitted_by", length = 15)
    @Enumerated(EnumType.STRING)
    private Source submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "created_at")
    LocalDateTime createdDate;

    @Column(name = "updated_at")
    LocalDateTime lastModifiedDate;

    @Column(name = "created_by")
    Long createdBy;

    @Column(name = "last_modified_by")
    Long lastModifiedBy;

    @Version
    @JsonIgnore
    private Long version;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
