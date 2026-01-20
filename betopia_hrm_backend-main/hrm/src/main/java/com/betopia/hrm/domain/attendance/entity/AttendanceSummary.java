package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.attendance.enums.AttendanceType;
import com.betopia.hrm.domain.attendance.enums.DayStatus;
import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.*;


import java.sql.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "attendance_summaries")
public class AttendanceSummary extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "attendance_policy_id")
    private Long attendancePolicyId;

    @Column(name = "work_date")
    private LocalDate workDate;

    @Column(name = "shift_id")
    private Long shiftId;

    @Column(name = "in_time")
    private LocalDateTime inTime;

    @Column(name = "out_time")
    private LocalDateTime outTime;

    @Column(name = "total_work_minutes")
    private Integer totalWorkMinutes;

    @Column(name = "late_minutes")
    private Integer lateMinutes;

    @Column(name = "early_leave_minutes")
    private Integer earlyLeaveMinutes;

    @Column(name = "overtime_minutes")
    private Integer overtimeMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_status", nullable = false)
    private DayStatus dayStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_type", nullable = false)
    private AttendanceType attendanceType;

    @Column(name = "manual_request_id", nullable = false)
    private Long manualRequestId;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "computed_at")
    private LocalDateTime computedAt;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "emp_serial_id")
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
