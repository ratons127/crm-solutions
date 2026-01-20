package com.betopia.hrm.services.attendance.attendanceDeviceAssign;

import com.betopia.hrm.domain.attendance.request.AttendanceDeviceAssignRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceAssignDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AttendanceDeviceAssignService {

    PaginationResponse<AttendanceDeviceAssignDTO> index(Sort.Direction direction, int page, int perPage);

    List<AttendanceDeviceAssignDTO> getAllAttendanceDeviceAssigns();

    AttendanceDeviceAssignDTO store(AttendanceDeviceAssignRequest request);

    AttendanceDeviceAssignDTO show(Long attendanceDeviceAssignId);

    AttendanceDeviceAssignDTO update(Long attendanceDeviceAssignId, AttendanceDeviceAssignRequest request);

    void destroy(Long attendanceDeviceAssignId);
}
