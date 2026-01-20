package com.betopia.hrm.webapp.util;

import com.betopia.hrm.domain.attendance.entity.AttendancePolicy;
import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.enums.DayStatus;
import com.betopia.hrm.domain.employee.entity.Employee;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class AttendanceUtil {


    /**
     * Calculate late minutes based on shift start time and policy grace minutes.
     */
    public static int calculateLateMinutes(ManualAttendance attendance, Shift shift, AttendancePolicy policy) {
        if (attendance.getInTime() == null) return 0;

        LocalTime shiftStart = shift.getStartTime();
        LocalTime allowedArrival = shiftStart.plusMinutes(policy.getGraceInMinutes());
        LocalTime inTime = attendance.getInTime().toLocalTime();

        if (inTime.isAfter(allowedArrival)) {
            return (int) Duration.between(allowedArrival, inTime).toMinutes();
        }
        return 0;
    }

    /**
     * Calculate early leave minutes based on shift end time and policy grace minutes.
     */
    public static int calculateEarlyLeaveMinutes(ManualAttendance attendance, Shift shift, AttendancePolicy policy) {
        if (attendance.getOutTime() == null) return 0;

        LocalTime shiftEnd = shift.getEndTime();
        LocalTime allowedLeave = shiftEnd.minusMinutes(policy.getGraceOutMinutes());
        LocalTime outTime = attendance.getOutTime().toLocalTime();

        if (outTime.isBefore(allowedLeave)) {
            return (int) Duration.between(outTime, allowedLeave).toMinutes();
        }
        return 0;
    }

    /**
     * Calculate total work minutes.
     */
    public static int calculateTotalWorkMinutes(LocalDateTime inTime, LocalDateTime outTime) {
        if (inTime == null || outTime == null) return 0;
        return (int) Duration.between(inTime, outTime).toMinutes();
    }

    /**
     * Calculate overtime minutes if total work exceeds policy min work minutes.
     */
    public static int calculateOvertimeMinutes(LocalDateTime outTime, Shift shift) {
        if (outTime == null || shift == null || shift.getEndTime() == null) {
            return 0;
        }

        LocalTime shiftEnd = shift.getEndTime();
        LocalDateTime shiftEndDateTime = outTime.toLocalDate().atTime(shiftEnd);

        if (outTime.isAfter(shiftEndDateTime)) {
            return (int) Duration.between(shiftEndDateTime, outTime).toMinutes();
        } else {
            return 0;
        }
    }



    public static DayStatus determineDayStatus(LocalTime inTime, LocalTime outTime, AttendancePolicy policy,Shift shift) {
        if (inTime == null && outTime == null) {
            return DayStatus.ABSENT;
        }

        // Office base timings (or fetch from shift if needed)
        LocalTime shiftStart = shift.getStartTime();
        LocalTime shiftEnd = shift.getEndTime();

        LocalTime graceInLimit = shiftStart.plusMinutes(policy.getGraceInMinutes());
        LocalTime graceOutLimit = shiftEnd.minusMinutes(policy.getGraceOutMinutes());

        boolean isLate = inTime != null && inTime.isAfter(graceInLimit);
        boolean isEarlyLeave = outTime != null && outTime.isBefore(graceOutLimit);

        long workedMinutes = 0;
        if (inTime != null && outTime != null) {
            workedMinutes = Duration.between(inTime, outTime).toMinutes();
        }

        if (workedMinutes < policy.getAbsenceThresholdMinutes()) {
            return DayStatus.ABSENT;
        } else if (workedMinutes < policy.getHalfDayThresholdMinutes()) {
            return DayStatus.HALF_DAY;
        } else if (isLate && isEarlyLeave) {
            return DayStatus.LATE; // or custom combined enum
        } else if (isLate) {
            return DayStatus.LATE;
        } else if (isEarlyLeave) {
            return DayStatus.EARLY_LEAVE;
        } else {
            return DayStatus.PRESENT;
        }
    }

    public static String getFullName(Employee employee) {
        if (employee == null) return "";
        return String.format("%s %s",
                employee.getFirstName() != null ? employee.getFirstName() : "",
                employee.getLastName() != null ? employee.getLastName() : ""
        ).trim();
    }



}
