package com.betopia.hrm.controllers.rest.v1.attendance;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceMonthlyReportDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceReportDTO;
import com.betopia.hrm.services.attendance.attendancereport.AttendanceReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.List;
import java.util.Date;



@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/attendance-report")
@Tag(
        name = "Attendance -> Attendance report",
        description = "APIs for managing attendance-report"
)
public class AttendanceReportController {
    private final AttendanceReportService attendanceReportService;

    public AttendanceReportController(AttendanceReportService attendanceReportService) {
        this.attendanceReportService = attendanceReportService;
    }
    @PreAuthorize("hasAuthority('show-report')")
    @GetMapping("/employee-report")
    @Operation(
            summary = "Get employee attendance report (date range wise)",
            description = """
                Retrieves attendance report for a specific employee within a given date range.
            """
    )
    public ResponseEntity<GlobalResponse> getEmployeeAttendanceReport(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long shiftId,
            @RequestParam(required = false) Long businessUnitId,
            @RequestParam(required = false) Long workplaceGroupId,
            @RequestParam(required = false) Long workplaceId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate
    ) {
        List<AttendanceReportDTO> report =
                attendanceReportService.getEmployeeAttendanceReport(
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

        GlobalResponse response = GlobalResponse.success(
                report,
                "Employee attendance report fetched successfully",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('show-report')")
    @GetMapping("/monthly-report")
    @Operation(
            summary = "Get employee attendance report (date range wise)",
            description = """
                Retrieves monthly attendance report for within a given date range.
            """
    )

    public ResponseEntity<GlobalResponse> getAttendanceMonthlyReport(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long businessUnitId,
            @RequestParam(required = false) Long workplaceGroupId,
            @RequestParam(required = false) Long workplaceId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long teamId
    ) {
        List<AttendanceMonthlyReportDTO> report = attendanceReportService.getAttendanceMonthlyReport(
                fromDate,
                toDate,
                companyId,
                businessUnitId,
                workplaceGroupId,
                workplaceId,
                departmentId,
                teamId
        );
        GlobalResponse response = GlobalResponse.success(
                report,
                "Employee attendance report fetched successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasAuthority('download-report')")
    @GetMapping("/export/employee-report")
    public ResponseEntity<byte[]> exportEmployeeAttendanceReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long businessUnitId,
            @RequestParam(required = false) Long workplaceGroupId,
            @RequestParam(required = false) Long workplaceId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) Long shiftId,
            @RequestParam(defaultValue = "excel") String format
    ) {
        try {
            byte[] data = attendanceReportService.exportEmployeeAttendanceReport(
                    fromDate, toDate, employeeId, businessUnitId,
                    workplaceGroupId, workplaceId, departmentId,
                    teamId, shiftId, format
            );

            HttpHeaders headers = new HttpHeaders();
            if ("csv".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("text/csv"));
            } else if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            String filename = "employee-attendance-report." +
                    (format.equalsIgnoreCase("excel") ? "xlsx" : format.toLowerCase());
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasAuthority('download-report')")
    @GetMapping("/export/monthly-report")
    public ResponseEntity<byte[]> exportAttendanceMonthlyReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long businessUnitId,
            @RequestParam(required = false) Long workplaceGroupId,
            @RequestParam(required = false) Long workplaceId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long teamId,
            @RequestParam(defaultValue = "excel") String format
    ) {
        try {
            byte[] data = attendanceReportService.exportAttendanceMonthlyReport(
                    fromDate, toDate,
                    companyId, businessUnitId, workplaceGroupId,
                    workplaceId, departmentId, teamId,
                    format
            );

            HttpHeaders headers = new HttpHeaders();
            if ("csv".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("text/csv"));
            } else if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            String filename = "attendance-monthly-summary." +
                    (format.equalsIgnoreCase("excel") ? "xlsx" : format.toLowerCase());
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
