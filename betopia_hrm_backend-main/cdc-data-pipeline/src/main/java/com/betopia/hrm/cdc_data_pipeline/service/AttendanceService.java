package com.betopia.hrm.cdc_data_pipeline.service;

import com.betopia.hrm.cdc_data_pipeline.util.TimeHelper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.betopia.hrm.cdc_data_pipeline.util.DataUtils.convertToSqlTime;
import static com.betopia.hrm.cdc_data_pipeline.util.DataUtils.safeDouble;
import static com.betopia.hrm.cdc_data_pipeline.util.DataUtils.safeInt;
import static com.betopia.hrm.cdc_data_pipeline.util.DataUtils.timestampOrNow;

@Service
public class AttendanceService {

    private final JdbcTemplate jdbcTemplate;

    public AttendanceService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> getEmployeeByCode(String empCode) {
        String sql = "SELECT * FROM employees WHERE employee_serial_id = ?";
        try {
            Integer empCodeInt = Integer.parseInt(empCode); 
            return jdbcTemplate.queryForMap(sql, empCodeInt);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getShiftIdByEmployeeId(Integer employeeId) {
        String sql = "SELECT shift_id FROM shift_assignments WHERE employee_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, employeeId);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getDefaultShiftId() {
        String sql = "SELECT id FROM shifts order by id limit 1";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    public Map<String, Object> getFirstAttendancePolicy() {
        String sql = "SELECT * FROM attendance_policy ORDER BY id ASC LIMIT 1";
        try {
            return jdbcTemplate.queryForMap(sql);
        } catch (Exception e) {
            return null;
        }
    }

    public LocalTime getShiftStartTimeById(Integer shiftId) {
        String sql = "SELECT start_time FROM shifts WHERE id = ?";
        try {
            Timestamp ts = jdbcTemplate.queryForObject(sql, Timestamp.class, shiftId);
            return ts.toLocalDateTime().toLocalTime();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean attendanceExists(String empCode, Timestamp punchTime) {
        // SQL query to check for attendance
        String checkSql = "SELECT id FROM iclock_transaction " +
                "WHERE emp_code = ? AND DATE(punch_time) = DATE(?) LIMIT 1";

        // Execute the query to check for an existing record
        List<Map<String, Object>> existing = jdbcTemplate.queryForList(checkSql, empCode, punchTime);

        // Return true if a record exists, false if not
        return !existing.isEmpty();
    }

    public void insertAttendance(Map<String, Object> data) {
        String sql = "INSERT INTO iclock_transaction (" +
                "emp_code, punch_time, punch_state, verify_type, work_code, " +
                "terminal_sn, terminal_alias, area_alias, longitude, latitude, gps_location, " +
                "mobile, source, purpose, crc, is_attendance, reserved, upload_time, " +
                "sync_status, is_mask, temperature, emp_id, terminal_id, company_code, " +
                "created_at, updated_at" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                data.get("emp_code"),
                convertToSqlTime(data.get("punch_time")),
                data.get("punch_state"),
                data.get("verify_type"),
                data.get("work_code"),
                data.get("terminal_sn"),
                data.get("terminal_alias"),
                data.get("area_alias"),
                safeDouble(data.get("longitude"), "longitude"),
                safeDouble(data.get("latitude"), "latitude"),
                data.get("gps_location"),
                data.get("mobile"),
                data.get("source"),
                data.get("purpose"),
                data.get("crc"),
                data.get("is_attendance"),
                data.get("reserved"),
                data.get("upload_time") == null ? null : convertToSqlTime(data.get("upload_time")),
                data.get("sync_status"),
                data.get("is_mask"),
                safeDouble(data.get("temperature"), "temperature"),
                data.get("emp_id"),
                data.get("terminal_id"),
                data.get("company_code"),
                timestampOrNow(data.get("created_at")),
                timestampOrNow(data.get("updated_at"))
        );
    }

    public void updateAttendanceByPunchTime(String empCode, Timestamp punchTime, Timestamp uploadTime) {
        String sql = "UPDATE iclock_transaction SET " +
                "punch_out = ?, upload_time = ?, updated_at = NOW() " +
                "WHERE emp_code = ? AND DATE(punch_time) = DATE(?)";

        jdbcTemplate.update(sql, punchTime, uploadTime, empCode, punchTime);
    }

    public void saveOrUpdateAttendanceSummary(Map<String, Object> data) {

        String empCode = (String) data.get("emp_code");
        Timestamp punchTimeStamp = convertToSqlTime(data.get("punch_time"));
        if (punchTimeStamp == null) {
            System.err.println("Punch time is null for emp_code: " + empCode);
            return;
        }

        // Load employee info
        Map<String, Object> employee = getEmployeeByCode(empCode);
        if (employee == null) {
            System.err.println("Employee not found for emp_code: " + empCode);
            return;
        }

        Integer employeeId = safeInt(employee,"id");
        Integer companyId = safeInt(employee,"company_id");
        Integer empSerialId = safeInt(employee,"employee_serial_id");

        // Attendance policy
        Map<String, Object> attendancePolicy = getFirstAttendancePolicy();
        Integer attendancePolicyId = attendancePolicy != null
                ? ((Number) attendancePolicy.get("id")).intValue()
                : 1;
        int graceMinutes = attendancePolicy != null && attendancePolicy.get("grace_in_minutes") != null
                ? ((Number) attendancePolicy.get("grace_in_minutes")).intValue()
                : 0;

        // Shift info
        Integer shiftId = getShiftIdByEmployeeId(employeeId);
        if (shiftId == null) {
            shiftId = getDefaultShiftId();
        }
        LocalTime shiftStartTime = getShiftStartTimeById(shiftId);

        // Extract punch date/time
        LocalDateTime punchDT = punchTimeStamp.toLocalDateTime();
        LocalDate punchDate = punchDT.toLocalDate();
        LocalTime punchLocalTime = punchDT.toLocalTime();

        // Early window = shiftStart - 2 hours
        LocalTime earlyWindowEnd = shiftStartTime.minusHours(2);

        // ---------------------------
        // FIX → CALCULATE workDate FIRST
        // ---------------------------
        LocalDate workDate;

        if (!punchLocalTime.isBefore(LocalTime.MIDNIGHT) &&
                punchLocalTime.isBefore(earlyWindowEnd)) {

            // midnight → early window punch belongs to previous day
            workDate = punchDate.minusDays(1);
            System.out.println("➡ midnight punch, workDate = " + workDate);

        } else {
            // normal punch
            workDate = punchDate;
            System.out.println("➡ normal punch, workDate = " + workDate);
        }

        // ---------------------------
        // NOW check DB using CORRECT workDate
        // ---------------------------
        String checkSql = "SELECT * FROM attendance_summaries WHERE employee_id = ? AND work_date=? LIMIT 1";

        Map<String, Object> latestSummary = null;
        try {
            latestSummary = jdbcTemplate.queryForMap(checkSql, employeeId, workDate);
        } catch (EmptyResultDataAccessException e) {
            // no previous summary
        }

        // ---------------------------
        // FIRST PUNCH → INSERT IN
        // ---------------------------
        if (latestSummary == null) {
            insertSummaryJdbc(
                    employeeId, empSerialId, companyId,
                    attendancePolicyId, shiftId,
                    shiftStartTime, punchTimeStamp,
                    graceMinutes, workDate
            );
            System.out.println("🟢 First IN created for " + punchTimeStamp + " emp_code=" + empCode);
            return;
        }

        // ---------------------------
        // Update OUT logic
        // ---------------------------
        Timestamp inTS = (Timestamp) latestSummary.get("in_time");
        Timestamp outTS = (Timestamp) latestSummary.get("out_time");

        LocalDateTime inTime = inTS.toLocalDateTime();
        LocalDateTime lastOutTime = outTS != null ? outTS.toLocalDateTime() : inTime;

        LocalDateTime earlyWindowLimit =
                inTime.toLocalDate().plusDays(1)
                        .atTime(shiftStartTime)
                        .minusHours(2);

        if (punchDT.isAfter(lastOutTime) && !punchDT.isAfter(earlyWindowLimit)) {

            System.out.println("🟢 Updating OUT");

            LocalDateTime outTime = punchDT;

            long totalWorkMinutes = Duration.between(inTime, outTime).toMinutes();

            long earlyLeaveMinutes = 0;
            long overtimeMinutes = 0;

            if (shiftStartTime != null) {
                LocalTime shiftEnd = shiftStartTime.plusHours(8);

                if (outTime.toLocalTime().isBefore(shiftEnd)) {
                    earlyLeaveMinutes = Duration.between(outTime.toLocalTime(), shiftEnd).toMinutes();
                } else if (outTime.toLocalTime().isAfter(shiftEnd)) {
                    overtimeMinutes = Duration.between(shiftEnd, outTime.toLocalTime()).toMinutes();
                }
            }

            long lateMinutes = 0;
            String dayStatus = "PRESENT";

            if (shiftStartTime != null) {
                LocalTime allowedTime = TimeHelper.addMinutes(shiftStartTime, graceMinutes, true);
                if (inTime.toLocalTime().isAfter(allowedTime)) {
                    lateMinutes = Duration.between(allowedTime, inTime.toLocalTime()).toMinutes();
                    dayStatus = "LATE";
                }
                if (earlyLeaveMinutes > 0) {
                    dayStatus = "EARLY_LEAVE";
                }
            }

            String updateSql = "UPDATE attendance_summaries SET out_time = ?, total_work_minutes = ?, " +
                    "early_leave_minutes = ?, overtime_minutes = ?, late_minutes = ?, day_status = ?, updated_at = ? " +
                    "WHERE id = ?";

            jdbcTemplate.update(updateSql,
                    Timestamp.valueOf(outTime),
                    totalWorkMinutes,
                    earlyLeaveMinutes,
                    overtimeMinutes,
                    lateMinutes,
                    dayStatus,
                    Timestamp.valueOf(LocalDateTime.now()),
                    ((Number) latestSummary.get("id")).intValue()
            );

            System.out.println("✅ OUT updated for employee_id=" + employeeId + " punch_time=" + punchDT);
            return;
        }

        // ---------------------------
        // New punch after window → new summary
        // ---------------------------
        insertSummaryJdbc(
                employeeId, empSerialId, companyId,
                attendancePolicyId, shiftId,
                shiftStartTime, punchTimeStamp,
                graceMinutes, workDate
        );
        System.out.println("🆕 New summary created for emp_code=" + empCode + " punch_time=" + punchTimeStamp);
    }


    // Helper method: insert IN only
    private void insertSummaryJdbc(Integer employeeId, Integer empSerialId, Integer companyId, Integer attendancePolicyId,
                                   Integer shiftId, LocalTime shiftStartTime, Timestamp inTimeStamp, int graceMinutes,LocalDate workDate) {

        if (inTimeStamp == null) {
            System.err.println("Cannot insert summary: inTimeStamp is null for employeeId " + employeeId);
            return;
        }

        LocalTime punchInTime = inTimeStamp.toLocalDateTime().toLocalTime();
//        LocalDate workDate = inTimeStamp.toLocalDateTime().toLocalDate();

        long lateMinutes = 0;
        String dayStatus = "PRESENT";
        if (shiftStartTime != null && punchInTime != null) {
            LocalTime allowedTime = shiftStartTime.plusMinutes(graceMinutes);
            if (punchInTime.isAfter(allowedTime)) {
                lateMinutes = Duration.between(allowedTime, punchInTime).toMinutes();
                dayStatus = "LATE";
            }
        } else {
            dayStatus = "ABSENT";
        }

        // Default Values
        String attendanceType = "DEVICE";
        boolean isLocked = false;

        String sql = "INSERT INTO attendance_summaries (" +
                "company_id, employee_id, emp_serial_id, attendance_policy_id, work_date, shift_id, in_time, " +
                "late_minutes, day_status, attendance_type, " +
                "computed_at, is_locked, created_at, updated_at" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                companyId,
                employeeId,
                empSerialId,
                attendancePolicyId,
                workDate,
                shiftId,
                inTimeStamp,
                lateMinutes,
                dayStatus,
                attendanceType,
                timestampOrNow(Timestamp.valueOf(LocalDateTime.now())),
                isLocked,
                timestampOrNow(Timestamp.valueOf(LocalDateTime.now())),
                timestampOrNow(Timestamp.valueOf(LocalDateTime.now()))
        );

        System.out.println("🟢 Attendance summary inserted for employeeId=" + employeeId + " IN=" + inTimeStamp);
    }

    /** Soft Delete **/
    public void softDelete(int id) {
        String sql = "UPDATE attendance SET updated_at = NOW(), remarks = 'Soft deleted' WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /** Hard Delete **/
    public void hardDelete(int id) {
        String sql = "DELETE FROM attendance WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
