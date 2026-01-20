package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttendanceDeviceAssignDTO {

    private Long id;

    private AttendanceDeviceDTO attendanceDevice;

    private EmployeeDTO employee;

    private Long deviceUserId;

    private Long assignedBy;

    private LocalDateTime assignedAt;

    private String note;

    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttendanceDeviceDTO getAttendanceDevice() {
        return attendanceDevice;
    }

    public void setAttendanceDevice(AttendanceDeviceDTO attendanceDevice) {
        this.attendanceDevice = attendanceDevice;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public Long getDeviceUserId() {
        return deviceUserId;
    }

    public void setDeviceUserId(Long deviceUserId) {
        this.deviceUserId = deviceUserId;
    }

    public Long getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
