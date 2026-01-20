package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.entity.Employee;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_shift_rotations",
        indexes = {
        @Index(name = "idx_employee_id", columnList = "employee_id"),
        @Index(name = "idx_employee_start_date", columnList = "employee_id, start_date")
})
public class ShiftEmployeeRotation extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pattern_id", nullable = false)
    private ShiftRotationPatterns pattern;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "cycle_start_day")
    private Integer cycleStartDay = 1;

    @Column(name = "status")
    private Boolean status = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ShiftRotationPatterns getPattern() {
        return pattern;
    }

    public void setPattern(ShiftRotationPatterns pattern) {
        this.pattern = pattern;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getCycleStartDay() {
        return cycleStartDay;
    }

    public void setCycleStartDay(Integer cycleStartDay) {
        this.cycleStartDay = cycleStartDay;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
