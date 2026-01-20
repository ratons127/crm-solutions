package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "shift_rotation_pattern_details")
public class ShiftRotationPatternDetail extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pattern_id", nullable = false)
    private ShiftRotationPatterns pattern;

    @Column(name = "day_number", nullable = false)
    private Long dayNumber;

    @Column(name = "shift_id")
    private Long shiftId;

    @Column(name = "is_off_day", columnDefinition = "boolean default false")
    private Boolean isOffDay = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShiftRotationPatterns getPattern() {
        return pattern;
    }

    public void setPattern(ShiftRotationPatterns pattern) {
        this.pattern = pattern;
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
