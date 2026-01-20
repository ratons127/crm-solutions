package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.entity.Employee;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_device_assign")
public class AttendanceDeviceAssign extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private AttendanceDevice attendanceDevice;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "device_user_id")
    private Long deviceUserId;

    @Column(name = "assigned_by")
    private Long assignedBy;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "notes")
    private String notes;

    @Column(name = "status")
    private Boolean status = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttendanceDevice getAttendanceDevice() {
        return attendanceDevice;
    }

    public void setAttendanceDevice(AttendanceDevice attendanceDevice) {
        this.attendanceDevice = attendanceDevice;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
