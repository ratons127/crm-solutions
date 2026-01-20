package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "attendance_policy")
public class AttendancePolicy extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "grace_in_minutes")
    private Integer graceInMinutes;

    @Column(name = "grace_out_minutes")
    private Integer graceOutMinutes;

    @Column(name = "late_threshold_minutes")
    private Integer lateThresholdMinutes;

    @Column(name = "early_leave_threshold_minutes")
    private Integer earlyLeaveThresholdMinutes;

    @Column(name = "min_work_minutes")
    private Integer minWorkMinutes;

    @Column(name = "half_day_threshold_minutes")
    private Integer halfDayThresholdMinutes;

    @Column(name = "absence_threshold_minutes")
    private Integer absenceThresholdMinutes;

    @Column(name = "movement_enabled")
    private Boolean movementEnabled;

    @Column(name = "movement_allow_minutes")
    private Integer movementAllowMinutes = 0;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "company_name")
    private String CompanyName;

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }

    public Integer getGraceInMinutes() { return graceInMinutes; }
    public void setGraceInMinutes(Integer graceInMinutes) { this.graceInMinutes = graceInMinutes; }

    public Integer getGraceOutMinutes() { return graceOutMinutes; }
    public void setGraceOutMinutes(Integer graceOutMinutes) { this.graceOutMinutes = graceOutMinutes; }

    public Integer getLateThresholdMinutes() { return lateThresholdMinutes; }
    public void setLateThresholdMinutes(Integer lateThresholdMinutes) { this.lateThresholdMinutes = lateThresholdMinutes; }

    public Integer getEarlyLeaveThresholdMinutes() { return earlyLeaveThresholdMinutes; }
    public void setEarlyLeaveThresholdMinutes(Integer earlyLeaveThresholdMinutes) { this.earlyLeaveThresholdMinutes = earlyLeaveThresholdMinutes; }

    public Integer getMinWorkMinutes() { return minWorkMinutes; }
    public void setMinWorkMinutes(Integer minWorkMinutes) { this.minWorkMinutes = minWorkMinutes; }

    public Integer getHalfDayThresholdMinutes() { return halfDayThresholdMinutes; }
    public void setHalfDayThresholdMinutes(Integer halfDayThresholdMinutes) { this.halfDayThresholdMinutes = halfDayThresholdMinutes; }

    public Integer getAbsenceThresholdMinutes() { return absenceThresholdMinutes; }
    public void setAbsenceThresholdMinutes(Integer absenceThresholdMinutes) { this.absenceThresholdMinutes = absenceThresholdMinutes; }

    public Boolean getMovementEnabled() { return movementEnabled; }
    public void setMovementEnabled(Boolean movementEnabled) { this.movementEnabled = movementEnabled; }

    public Integer getMovementAllowMinutes() { return movementAllowMinutes; }
    public void setMovementAllowMinutes(Integer movementAllowMinutes) { this.movementAllowMinutes = movementAllowMinutes; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
