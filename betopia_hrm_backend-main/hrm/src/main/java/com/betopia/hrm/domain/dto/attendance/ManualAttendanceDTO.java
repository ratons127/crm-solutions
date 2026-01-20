package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.attendance.enums.AdjustmentType;
import com.betopia.hrm.domain.attendance.enums.AttendanceStatus;
import com.betopia.hrm.domain.attendance.enums.Source;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ManualAttendanceDTO {

    private Long id;
    private Long employeeId;
    private LocalDate attendanceDate;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private AdjustmentType adjustmentType;
    private String reason;
    private Long submittedById;
    private LocalDateTime submittedAt;;
    private Source sourceChannel;
    private Boolean isLocked;
    private AttendanceStatus manualAttendanceStatus;

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

    public Long getSubmittedById() { return submittedById; }
    public void setSubmittedById(Long submittedById) { this.submittedById = submittedById; }

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
}
