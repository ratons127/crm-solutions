package com.betopia.hrm.domain.dto.attendance;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ShiftWeeklyOffDTO {

    private Long id;
    private String dayOfWeek;

    // Constructors
    public ShiftWeeklyOffDTO() {}

    public ShiftWeeklyOffDTO(Long id, String dayOfWeek) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
