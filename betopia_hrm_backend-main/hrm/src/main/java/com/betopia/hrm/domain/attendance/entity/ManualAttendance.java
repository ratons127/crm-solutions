package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.attendance.enums.AdjustmentType;
import com.betopia.hrm.domain.attendance.enums.AttendanceStatus;
import com.betopia.hrm.domain.attendance.enums.Source;
import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "manual_attendance")
public class ManualAttendance  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "in_time")
    private LocalDateTime inTime;

    @Column(name = "out_time")
    private LocalDateTime outTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type", nullable = false)
    private AdjustmentType adjustmentType;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "submitted_by", nullable = false)
    private Long submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "source_channel")
    private Source sourceChannel;

    @Enumerated(EnumType.STRING)
    @Column(name = "manual_attendance_status")
    private AttendanceStatus manualAttendanceStatus;

    @Column(name = "is_locked")
    private Boolean isLocked;

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

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }

    public LocalDateTime getInTime() { return inTime; }
    public void setInTime(LocalDateTime inTime) { this.inTime = inTime; }

    public LocalDateTime getOutTime() { return outTime; }
    public void setOutTime(LocalDateTime outTime) { this.outTime = outTime; }

    public AdjustmentType getAdjustmentType() { return adjustmentType; }
    public void setAdjustmentType(AdjustmentType adjustmentType) { this.adjustmentType = adjustmentType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Long getSubmittedBy() { return submittedBy; }
    public void setSubmittedBy(Long submittedBy) { this.submittedBy = submittedBy; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public Source getSourceChannel() { return sourceChannel; }
    public void setSourceChannel(Source sourceChannel) { this.sourceChannel = sourceChannel; }

    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }

    public AttendanceStatus getManualAttendanceStatus() {
        return manualAttendanceStatus;
    }

    public void setManualAttendanceStatus(AttendanceStatus manualAttendanceStatus) {
        this.manualAttendanceStatus = manualAttendanceStatus;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
