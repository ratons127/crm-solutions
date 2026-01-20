package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.AttendanceSummary;
import com.betopia.hrm.domain.dto.attendance.DeviceTypeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long> {

//    @Query(value = """
//            SELECT
//                TO_CHAR(work_date, 'YYYY-MM-DD') AS workDate,
//                TO_CHAR(in_time, 'HH12:MI AM') AS inTime,
//                TO_CHAR(out_time, 'HH12:MI PM') AS outTime,
//                CONCAT(
//                    (total_work_minutes / 60), ' hours ',
//                    (total_work_minutes % 60), ' minutes'
//                ) AS totalWorkDuration
//            FROM attendance_summaries
//            WHERE employee_id = :employeeId
//            ORDER BY work_date DESC
//            LIMIT :limit
//            """, nativeQuery = true)
    @Query(value = """
        SELECT
            TO_CHAR(work_date, 'FMMonth DD, YYYY') AS workDate,
            TO_CHAR(in_time AT TIME ZONE 'Asia/Dhaka', 'YYYY-MM-DD HH12:MI:SS AM') AS inTime,
            TO_CHAR(out_time AT TIME ZONE 'Asia/Dhaka', 'YYYY-MM-DD HH12:MI:SS AM') AS outTime,
            CONCAT(
                LPAD((total_work_minutes / 60)::TEXT, 2, '0'), ' hours ',
                LPAD((total_work_minutes % 60)::TEXT, 2, '0'), ' minutes'
            ) AS totalWorkDuration
        FROM attendance_summaries
        WHERE employee_id = :employeeId
        ORDER BY work_date DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<DeviceTypeDTO> findRecentAttendanceByEmployee(
            @Param("employeeId") Long employeeId,
            @Param("limit") int limit
    );
}
