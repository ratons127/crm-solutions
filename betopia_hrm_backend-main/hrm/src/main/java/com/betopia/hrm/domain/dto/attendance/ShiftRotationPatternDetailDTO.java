package com.betopia.hrm.domain.dto.attendance;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ShiftRotationPatternDetailDTO {

    private Long id;

    private Long dayNumber;

    private Long shiftId;

    private Boolean isOffDay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Long dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Long getShiftId() {
        return shiftId;
    }

    public void setShiftId(Long shiftId) {
        this.shiftId = shiftId;
    }

    public Boolean getOffDay() {
        return isOffDay;
    }

    public void setOffDay(Boolean offDay) {
        isOffDay = offDay;
    }
}
