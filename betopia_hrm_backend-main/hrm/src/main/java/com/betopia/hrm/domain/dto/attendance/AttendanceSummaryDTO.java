package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.attendance.enums.AttendanceType;
import com.betopia.hrm.domain.attendance.enums.DayStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttendanceSummaryDTO {
    private Long id;
    private Long companyId;
    private Long employeeId;
    private Long attendancePolicyId;
    private LocalDate workDate;
    private Long shiftId;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private Integer totalWorkMinutes;
    private Integer lateMinutes;
    private Integer earlyLeaveMinutes;
    private Integer overtimeMinutes;
    private DayStatus dayStatus;
    private AttendanceType attendanceType;
    private Long manualRequestId;
    private String remarks;
    private LocalDateTime computedAt;
    private Boolean isLocked;
    private Long employeeSerialId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getAttendancePolicyId() {
        return attendancePolicyId;
    }

    public void setAttendancePolicyId(Long attendancePolicyId) {
        this.attendancePolicyId = attendancePolicyId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
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

    public Integer getTotalWorkMinutes() {
        return totalWorkMinutes;
    }

    public void setTotalWorkMinutes(Integer totalWorkMinutes) {
        this.totalWorkMinutes = totalWorkMinutes;
    }

    public Integer getLateMinutes() {
        return lateMinutes;
    }

    public void setLateMinutes(Integer lateMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public Integer getEarlyLeaveMinutes() {
        return earlyLeaveMinutes;
    }

    public void setEarlyLeaveMinutes(Integer earlyLeaveMinutes) {
        this.earlyLeaveMinutes = earlyLeaveMinutes;
    }

    public Integer getOvertimeMinutes() {
        return overtimeMinutes;
    }

    public void setOvertimeMinutes(Integer overtimeMinutes) {
        this.overtimeMinutes = overtimeMinutes;
    }

    public DayStatus getDayStatus() {
        return dayStatus;
    }

    public void setDayStatus(DayStatus dayStatus) {
        this.dayStatus = dayStatus;
    }

    public AttendanceType getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(AttendanceType attendanceType) {
        this.attendanceType = attendanceType;
    }

    public Long getManualRequestId() {
        return manualRequestId;
    }

    public void setManualRequestId(Long manualRequestId) {
        this.manualRequestId = manualRequestId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getComputedAt() {
        return computedAt;
    }

    public void setComputedAt(LocalDateTime computedAt) {
        this.computedAt = computedAt;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean locked) {
        isLocked = locked;
    }

    public Long getEmployeeSerialId() {
        return employeeSerialId;
    }

    public void setEmployeeSerialId(Long employeeSerialId) {
        this.employeeSerialId = employeeSerialId;
    }
}
