package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.dto.company.CompanyDTO;

import java.time.LocalTime;
import java.util.List;

public class ShiftDTO {

    private Long id;
    private Long shiftCategoryId;
    private String shiftName;
    private String shiftCode;

    private Long companyId;

    private LocalTime startTime;
    private LocalTime endTime;
    private Integer breakMinutes;
    private Boolean isNightShift;
    private Integer graceInMinutes;
    private Integer graceOutMinutes;
    private Boolean status;

    private List<ShiftWeeklyOffDTO> weeklyOffs;

    public ShiftDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }


    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getBreakMinutes() {
        return breakMinutes;
    }

    public void setBreakMinutes(Integer breakMinutes) {
        this.breakMinutes = breakMinutes;
    }

    public Boolean getNightShift() {
        return isNightShift;
    }

    public void setNightShift(Boolean nightShift) {
        isNightShift = nightShift;
    }

    public Integer getGraceInMinutes() {
        return graceInMinutes;
    }

    public void setGraceInMinutes(Integer graceInMinutes) {
        this.graceInMinutes = graceInMinutes;
    }

    public Integer getGraceOutMinutes() {
        return graceOutMinutes;
    }

    public void setGraceOutMinutes(Integer graceOutMinutes) {
        this.graceOutMinutes = graceOutMinutes;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ShiftWeeklyOffDTO> getWeeklyOffs() {
        return weeklyOffs;
    }

    public void setWeeklyOffs(List<ShiftWeeklyOffDTO> weeklyOffs) {
        this.weeklyOffs = weeklyOffs;
    }

    public Long getShiftCategoryId() {
        return shiftCategoryId;
    }

    public void setShiftCategoryId(Long shiftCategoryId) {
        this.shiftCategoryId = shiftCategoryId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
