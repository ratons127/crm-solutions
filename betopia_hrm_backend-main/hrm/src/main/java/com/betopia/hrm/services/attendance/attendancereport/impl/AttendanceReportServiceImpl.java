package com.betopia.hrm.services.attendance.attendancereport.impl;

import com.betopia.hrm.domain.attendance.repository.AttendanceReportRepository;
import com.betopia.hrm.domain.dto.attendance.AttendanceMonthlyReportDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceReportDTO;
import com.betopia.hrm.services.attendance.attendancereport.AttendanceMonthlyReportFileService;
import com.betopia.hrm.services.attendance.attendancereport.AttendanceReportFileService;
import com.betopia.hrm.services.attendance.attendancereport.AttendanceReportService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AttendanceReportServiceImpl implements AttendanceReportService {

    private final AttendanceReportRepository attendanceReportRepository;
    private final AttendanceReportFileService attendanceReportFileService;
    private final AttendanceMonthlyReportFileService attendanceMonthlyReportFileService;


    public AttendanceReportServiceImpl(AttendanceReportRepository attendanceReportRepository,
                                       AttendanceReportFileService attendanceReportFileService,
                                       AttendanceMonthlyReportFileService attendanceMonthlyReportFileService) {
        this.attendanceReportRepository = attendanceReportRepository;
        this.attendanceReportFileService = attendanceReportFileService;
        this.attendanceMonthlyReportFileService = attendanceMonthlyReportFileService;
    }

    @Override
    public List<AttendanceReportDTO> getEmployeeAttendanceReport(
            Long employeeId,
            Long shiftId,
            Long businessUnitId,
            Long workplaceGroupId,
            Long workplaceId,
            Long departmentId,
            Long teamId,
            Date fromDate,
            Date toDate
    ) {
        if (employeeId == null) {
            throw new RuntimeException("Employee ID cannot be null");
        }
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Date range cannot be null");
        }
        if (fromDate.after(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date");
        }

        return attendanceReportRepository.findEmployeeAttendanceReport(
                employeeId,
                shiftId,
                businessUnitId,
                workplaceGroupId,
                workplaceId,
                departmentId,
                teamId,
                fromDate,
                toDate
        );
    }

    @Override
    public List<AttendanceMonthlyReportDTO> getAttendanceMonthlyReport(
            Date fromDate,
            Date toDate,
            Long companyId,
            Long businessUnitId,
            Long workplaceGroupId,
            Long workplaceId,
            Long departmentId,
            Long teamId
    ) {
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("From Date and To Date are required.");
        }

        return attendanceReportRepository.findAttendanceMonthlyReport(
                fromDate,
                toDate,
                companyId,
                businessUnitId,
                workplaceGroupId,
                workplaceId,
                departmentId,
                teamId
        );
    }

    @Override
    public byte[] exportEmployeeAttendanceReport(
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
    ) throws Exception {

        List<AttendanceReportDTO> reports = attendanceReportRepository.findEmployeeAttendanceReport(
                employeeId,
                shiftId,
                businessUnitId,
                workplaceGroupId,
                workplaceId,
                departmentId,
                teamId,
                fromDate,
                toDate
        );

        if (reports == null || reports.isEmpty()) {
            throw new IllegalArgumentException("No records found.");
        }

        if ("excel".equalsIgnoreCase(format)) {
            return attendanceReportFileService.exportToExcel(reports).toByteArray();
        } else if ("csv".equalsIgnoreCase(format)) {
            return attendanceReportFileService.exportToCsv(reports).toByteArray();
        } else if ("pdf".equalsIgnoreCase(format)) {
            return attendanceReportFileService.exportToPdf(reports).toByteArray();
        } else {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }
    @Override
    public byte[] exportAttendanceMonthlyReport(
            Date fromDate,
            Date toDate,
            Long companyId,
            Long businessUnitId,
            Long workplaceGroupId,
            Long workplaceId,
            Long departmentId,
            Long teamId,
            String format
    ) throws Exception {

        List<AttendanceMonthlyReportDTO> reports = attendanceReportRepository.findAttendanceMonthlyReport(
                fromDate,
                toDate,
                companyId,
                businessUnitId,
                workplaceGroupId,
                workplaceId,
                departmentId,
                teamId
        );

        if (reports == null || reports.isEmpty()) {
            throw new IllegalArgumentException("No records found.");
        }

        if ("excel".equalsIgnoreCase(format)) {
            return attendanceMonthlyReportFileService.exportToExcel(reports).toByteArray();
        } else if ("csv".equalsIgnoreCase(format)) {
            return attendanceMonthlyReportFileService.exportToCsv(reports).toByteArray();
        } else if ("pdf".equalsIgnoreCase(format)) {
            return attendanceMonthlyReportFileService.exportToPdf(reports).toByteArray();
        } else {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }

}
