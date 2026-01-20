package com.betopia.hrm.domain.dto.attendance;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttendancePolicyDTO {

    private Long id;
    private Long companyId;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Integer graceInMinutes;
    private Integer graceOutMinutes;
    private Integer lateThresholdMinutes;
    private Integer earlyLeaveThresholdMinutes;
    private Integer minWorkMinutes;
    private Integer halfDayThresholdMinutes;
    private Integer absenceThresholdMinutes;
    private Boolean movementEnabled;
    private Integer movementAllowMinutes;
    private String notes;
    private Boolean status;
    private String companyName;

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
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
