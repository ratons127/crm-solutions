package com.betopia.hrm.services.attendance.attendancereport;

import com.betopia.hrm.domain.dto.attendance.AttendanceReportDTO;
import com.betopia.hrm.services.base.HrmFileServiceImpl;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

@Service
public class AttendanceReportFileService extends HrmFileServiceImpl<AttendanceReportDTO> {

    @Override
    public Class<AttendanceReportDTO> getEntityClass() {
        return AttendanceReportDTO.class;
    }
}