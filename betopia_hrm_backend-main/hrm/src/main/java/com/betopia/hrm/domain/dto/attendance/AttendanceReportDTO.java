package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.base.annotation.ExposeField;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttendanceReportDTO {

    @ExposeField
    private Integer employeeId;
    @ExposeField
    private String employeeName;
    @ExposeField
    private Date date;
    @ExposeField
    private String shiftName;
    @ExposeField
    private String inTime;
    @ExposeField
    private String outTime;
    @ExposeField
    private String totalWorkingHours;
    @ExposeField
    private String lateMinutes;
    @ExposeField
    private String earlyLeaveMinutes;
    @ExposeField
    private String dayStatus;

    public AttendanceReportDTO() {}

    public AttendanceReportDTO(Integer employeeId, String employeeName,
                               Date date, String shiftName, String inTime,
                               String outTime, String totalWorkingHours,
                               String lateMinutes, String earlyLeaveMinutes, String dayStatus) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.date = date;
        this.shiftName = shiftName;
        this.inTime = inTime;
        this.outTime = outTime;
        this.totalWorkingHours = totalWorkingHours;
        this.lateMinutes = lateMinutes;
        this.earlyLeaveMinutes = earlyLeaveMinutes;
        this.dayStatus = dayStatus;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public Date getDate() {
        return date;
    }

    public String getShiftName() {
        return shiftName;
    }

    public String getInTime() {
        return inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public String getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public String getLateMinutes() {
        return lateMinutes;
    }

    public String getEarlyLeaveMinutes() {
        return earlyLeaveMinutes;
    }

    public String getDayStatus() {
        return dayStatus;
    }
}
