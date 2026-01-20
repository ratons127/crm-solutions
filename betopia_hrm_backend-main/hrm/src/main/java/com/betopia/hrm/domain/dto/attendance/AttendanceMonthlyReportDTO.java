package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.base.annotation.ExposeField;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttendanceMonthlyReportDTO {
    @ExposeField
    private Integer employeeId;
    @ExposeField
    private String employeeName;
    @ExposeField
    private String designationName;
    @ExposeField
    private Long totalWorkingDays;
    @ExposeField
    private Long presentDays;
    @ExposeField
    private Long leaveDays;
    @ExposeField
    private Long absentDays;
    @ExposeField
    private Long lateDays;
    @ExposeField
    private Long earlyLeaveDays;
    @ExposeField
    private Long manualAttendance;
    @ExposeField
    private Long offDays;
    @ExposeField
    private Long holidays;

    public AttendanceMonthlyReportDTO(Integer employeeId, String employeeName,
                                      String designationName, Long totalWorkingDays,
                                      Long presentDays, Long leaveDays, Long absentDays,
                                      Long lateDays, Long earlyLeaveDays, Long manualAttendance,
                                      Long offDays, Long holidays) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.designationName = designationName;
        this.totalWorkingDays = totalWorkingDays;
        this.presentDays = presentDays;
        this.leaveDays = leaveDays;
        this.absentDays = absentDays;
        this.lateDays = lateDays;
        this.earlyLeaveDays = earlyLeaveDays;
        this.manualAttendance = manualAttendance;
        this.offDays = offDays;
        this.holidays = holidays;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public Long getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public void setTotalWorkingDays(Long totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public Long getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(Long presentDays) {
        this.presentDays = presentDays;
    }

    public Long getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Long leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Long getAbsentDays() {
        return absentDays;
    }

    public void setAbsentDays(Long absentDays) {
        this.absentDays = absentDays;
    }

    public Long getLateDays() {
        return lateDays;
    }

    public void setLateDays(Long lateDays) {
        this.lateDays = lateDays;
    }

    public Long getEarlyLeaveDays() {
        return earlyLeaveDays;
    }

    public void setEarlyLeaveDays(Long earlyLeaveDays) {
        this.earlyLeaveDays = earlyLeaveDays;
    }

    public Long getManualAttendance() {
        return manualAttendance;
    }

    public void setManualAttendance(Long manualAttendance) {
        this.manualAttendance = manualAttendance;
    }

    public Long getOffDays() {
        return offDays;
    }

    public void setOffDays(Long offDays) {
        this.offDays = offDays;
    }

    public Long getHolidays() {
        return holidays;
    }

    public void setHolidays(Long holidays) {
        this.holidays = holidays;
    }
}
