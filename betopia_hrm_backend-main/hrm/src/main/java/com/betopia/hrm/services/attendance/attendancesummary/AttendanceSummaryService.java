package com.betopia.hrm.services.attendance.attendancesummary;

import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.attendance.request.AttendanceSummaryRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceStatusDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceSummaryDTO;
import com.betopia.hrm.domain.dto.attendance.DeviceTypeDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AttendanceSummaryService {

    PaginationResponse<AttendanceSummaryDTO> index(Sort.Direction direction, int page, int perPage);

    List<AttendanceSummaryDTO> getAllAttendanceSummaries();

    AttendanceSummaryDTO store(AttendanceSummaryRequest request);

    AttendanceSummaryDTO show(Long attendanceSummaryId);

    AttendanceSummaryDTO update(Long attendanceSummaryId, AttendanceSummaryRequest request);

    void destroy(Long attendanceSummaryId);

    AttendanceSummaryDTO createInitialAttendanceSummary(ManualAttendance manualAttendance, Long empSerialId);

    List<DeviceTypeDTO> getRecentAttendance(Long employeeId, Integer limit);
    List<AttendanceStatusDTO> getAttendanceStatus();
}
