package com.betopia.hrm.domain.company.entity;

import com.betopia.hrm.domain.company.enums.WeekendType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendar_holidays")
public class CalendarHolidays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendars calendar;

    @Column(name = "working_type", nullable = false)
    private Integer workingType;        //0 = working, 1 = weekend, 2 = public holiday

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    @Column(name = "is_holiday", nullable = false)
    private Boolean isHoliday;      // 0 = working, 1 = holiday

    @Column(name = "color_code", length = 50)
    private String colorCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", nullable = false)
    private Boolean status = true; // 0 = inactive, 1 = active

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "weekend_type")
    private WeekendType weekendType;


    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Calendars getCalendar() { return calendar; }
    public void setCalendar(Calendars calendar) { this.calendar = calendar; }

    public Integer getWorkingType() { return workingType; }
    public void setWorkingType(Integer workingType) { this.workingType = workingType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getHolidayDate() { return holidayDate; }
    public void setHolidayDate(LocalDate holidayDate) { this.holidayDate = holidayDate; }

    public Boolean getIsHoliday() { return isHoliday; }
    public void setIsHoliday(Boolean isHoliday) { this.isHoliday = isHoliday; }

    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }

    public WeekendType getWeekendType() {
        return weekendType;
    }

    public void setWeekendType(WeekendType weekendType) {
        this.weekendType = weekendType;
    }

}
