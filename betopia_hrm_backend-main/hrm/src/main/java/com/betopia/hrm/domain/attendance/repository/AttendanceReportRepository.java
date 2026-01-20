package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.AttendanceSummary;
import com.betopia.hrm.domain.dto.attendance.AttendanceMonthlyReportDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceReportDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceReportRepository extends JpaRepository<AttendanceSummary, Long> {

    @Query(value = """
    SELECT
      e.id AS "employeeId",
      CONCAT(e.first_name, ' ', e.last_name) AS "employeeName",
        ch.holiday_date AS "date",
        CASE
            WHEN s.shift_name IS NOT NULL THEN
                CONCAT(
                    s.shift_name,
                    ' (',
                    TO_CHAR(s.start_time, 'HH12:MI AM'),
                    '–',
                    TO_CHAR(s.end_time, 'HH12:MI AM'),
                    ')'
                )
            ELSE NULL
        END AS "shiftName",

        CASE
            WHEN a.in_time IS NOT NULL THEN  TO_CHAR(a.in_time AT TIME ZONE 'Asia/Dhaka', 'YYYY-MM-DD HH12:MI:SS AM')
            ELSE NULL
        END AS "inTime",

        CASE
            WHEN a.out_time IS NOT NULL THEN TO_CHAR(a.out_time AT TIME ZONE 'Asia/Dhaka', 'YYYY-MM-DD HH12:MI:SS AM')
            ELSE NULL
        END AS "outTime",

        CASE
            WHEN a.total_work_minutes IS NOT NULL THEN
             CONCAT(
                LPAD((total_work_minutes / 60)::TEXT, 2, '0'), ' hours ',
                LPAD((total_work_minutes % 60)::TEXT, 2, '0'), ' minutes'
            )
            ELSE NULL
        END AS "totalWorkingHours",

        CASE
            WHEN a.late_minutes IS NOT NULL THEN
              CONCAT(
                LPAD((a.late_minutes / 60)::TEXT, 2, '0'), ' hours ',
                LPAD((a.late_minutes % 60)::TEXT, 2, '0'), ' minutes'
            )
            ELSE NULL
        END AS "lateMinutes",
        
        CASE
             WHEN a.early_leave_minutes IS NOT NULL THEN
                CONCAT(
                    LPAD((a.early_leave_minutes / 60)::TEXT, 2, '0'), ' hours ',
                    LPAD((a.early_leave_minutes % 60)::TEXT, 2, '0'), ' minutes'
            )
             ELSE NULL
        END AS "earlyLeaveMinutes",

        CASE
               WHEN a.day_status IS NOT NULL THEN a.day_status
               WHEN ch.working_type = 2 THEN 'PUBLIC HOLIDAY'
               WHEN ch.working_type = 1 THEN 'WEEKEND'
               WHEN sub.day_status = 'LEAVE' THEN 'LEAVE'
               WHEN ch.working_type = 0 THEN 'WORKING'
               ELSE 'WORKING'
           END AS "dayStatus"

    FROM calendar_holidays ch

    LEFT JOIN (
        SELECT
            l.employee_id,
            generate_series(l.start_date, l.end_date, interval '1 day')::date AS holiday_date,
            'LEAVE' AS day_status
        FROM leave_requests l
        WHERE l.status = 'APPROVED'
    ) AS sub
        ON ch.holiday_date = sub.holiday_date
        AND (:employeeId IS NULL OR sub.employee_id = :employeeId)

    LEFT JOIN attendance_summaries a
        ON a.work_date = ch.holiday_date
        AND (:employeeId IS NULL OR a.employee_id = :employeeId)

    LEFT JOIN employees e
        ON e.id = COALESCE(a.employee_id, sub.employee_id)

    LEFT JOIN shifts s
        ON s.id = e.shift_id

    WHERE
        ch.holiday_date BETWEEN :fromDate AND :toDate
        AND (:businessUnitId IS NULL OR e.business_unit_id = :businessUnitId)
        AND (:workplaceGroupId IS NULL OR e.work_place_group_id = :workplaceGroupId)
        AND (:workplaceId IS NULL OR e.workplace_id = :workplaceId)
        AND (:departmentId IS NULL OR e.department_id = :departmentId)
        AND (:teamId IS NULL OR e.team_id = :teamId)
        AND (:shiftId IS NULL OR e.shift_id = :shiftId)

    ORDER BY ch.holiday_date ASC
""", nativeQuery = true)

    List<AttendanceReportDTO> findEmployeeAttendanceReport(
            @Param("employeeId") Long employeeId,
            @Param("shiftId") Long shiftId,
            @Param("businessUnitId") Long businessUnitId,
            @Param("workplaceGroupId") Long workplaceGroupId,
            @Param("workplaceId") Long workplaceId,
            @Param("departmentId") Long departmentId,
            @Param("teamId") Long teamId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate
    );

    @Query(value = """
        SELECT
            e.id AS "employeeId",
            CONCAT(e.first_name, ' ', e.last_name) AS "employeeName",
            d.name AS "designationName",

            COALESCE(SUM(CASE WHEN ch.working_type NOT IN (1, 2) THEN 1 ELSE 0 END), 0) AS "totalWorkingDays",
            COALESCE(SUM(CASE WHEN a.day_status IN ('PRESENT', 'LATE') THEN 1 ELSE 0 END), 0) AS "presentDays",
            COALESCE(SUM(CASE WHEN lr.status = 'APPROVED'
                               AND ch.holiday_date BETWEEN lr.start_date AND lr.end_date
                         THEN 1 ELSE 0 END), 0) AS "leaveDays",
            COALESCE(SUM(CASE WHEN a.day_status = 'ABSENT' THEN 1 ELSE 0 END), 0) AS "absentDays",
            COALESCE(SUM(CASE WHEN a.day_status = 'LATE' THEN 1 ELSE 0 END), 0) AS "lateDays",
            COALESCE(SUM(CASE WHEN a.early_leave_minutes > 0 THEN 1 ELSE 0 END), 0) AS "earlyLeaveDays",
            COALESCE(SUM(CASE WHEN a.attendance_type = 'MANUAL' THEN 1 ELSE 0 END), 0) AS "manualAttendance",
            COALESCE(SUM(CASE WHEN ch.working_type = 1 THEN 1 ELSE 0 END), 0) AS "offDays",
            COALESCE(SUM(CASE WHEN ch.working_type = 2 THEN 1 ELSE 0 END), 0) AS "holidays"

        FROM public.employees e
        LEFT JOIN public.designations d ON e.designation_id = d.id
        CROSS JOIN public.calendar_holidays ch
        LEFT JOIN public.attendance_summaries a
               ON a.employee_id = e.id
              AND a.work_date = ch.holiday_date
        LEFT JOIN public.leave_requests lr
               ON lr.employee_id = e.id
              AND ch.holiday_date BETWEEN lr.start_date AND lr.end_date
              AND lr.status = 'APPROVED'
        WHERE ch.holiday_date BETWEEN :fromDate AND :toDate
    
               AND (:companyId IS NULL OR e.company_id = :companyId)
               AND (:businessUnitId IS NULL OR e.business_unit_id = :businessUnitId)
               AND (:workplaceGroupId IS NULL OR e.work_place_group_id = :workplaceGroupId)
               AND (:workplaceId IS NULL OR e.workplace_id = :workplaceId)
               AND (:departmentId IS NULL OR e.department_id = :departmentId)
               AND (:teamId IS NULL OR e.team_id = :teamId)
    
    
        GROUP BY e.id, e.first_name, e.last_name, d.name
        ORDER BY e.id
    """, nativeQuery = true)

    List<AttendanceMonthlyReportDTO> findAttendanceMonthlyReport(
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("companyId") Long companyId,
            @Param("businessUnitId") Long businessUnitId,
            @Param("workplaceGroupId") Long workplaceGroupId,
            @Param("workplaceId") Long workplaceId,
            @Param("departmentId") Long departmentId,
            @Param("teamId") Long teamId
    );
}

