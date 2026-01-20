package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ShiftEmployeeRotationDTO {

    private Long id;

    //Simple DTO instead of Entity
    private EmployeeDTO employee;

    //Simple DTO instead of Entity
    private ShiftRotationPatternDTO pattern;

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer cycleStartDay;
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public ShiftRotationPatternDTO getPattern() {
        return pattern;
    }

    public void setPattern(ShiftRotationPatternDTO pattern) {
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
