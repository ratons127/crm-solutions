package com.betopia.hrm.services.attendance.attendancereport;


import com.betopia.hrm.domain.dto.attendance.AttendanceMonthlyReportDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceReportDTO;
import java.util.Date;
import java.util.List;

public interface AttendanceReportService {

    List<AttendanceReportDTO> getEmployeeAttendanceReport(
                                                Long employeeId,
                                                Long shiftId,
                                                Long businessUnitId,
                                                Long workplaceGroupId,
                                                Long workplaceId,
                                                Long departmentId,
                                                Long teamId,
                                                Date fromDate,
                                                Date toDate );



    List<AttendanceMonthlyReportDTO> getAttendanceMonthlyReport(
                                                Date fromDate,
                                                Date toDate,
                                                Long companyId,
                                                Long businessUnitId,
                                                Long workplaceGroupId,
                                                Long workplaceId,
                                                Long departmentId,
                                                Long teamId
                                        );


    byte[] exportEmployeeAttendanceReport(
            Date fromDate,
            Date toDate,
            Long employeeId,
            Long businessUnitId,
            Long workplaceGroupId,
            Long workplaceId,
            Long departmentId,
            Long teamId,
            Long shiftId,
            String format
    ) throws Exception;


    byte[] exportAttendanceMonthlyReport(
            Date fromDate,
            Date toDate,
            Long companyId,
            Long businessUnitId,
            Long workplaceGroupId,
            Long workplaceId,
            Long departmentId,
            Long teamId,
            String format
    ) throws Exception;
}
