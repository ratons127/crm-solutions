package com.betopia.hrm.services.attendance.attendancereport;

import com.betopia.hrm.domain.dto.attendance.AttendanceMonthlyReportDTO;
import com.betopia.hrm.services.base.HrmFileServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AttendanceMonthlyReportFileService extends HrmFileServiceImpl<AttendanceMonthlyReportDTO> {

    @Override
    public Class<AttendanceMonthlyReportDTO> getEntityClass() {
        return AttendanceMonthlyReportDTO.class;
    }
}