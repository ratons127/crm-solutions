package com.betopia.hrm.services.attendance.attendanceDevice;

import com.betopia.hrm.domain.attendance.request.AttendanceDeviceRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AttendanceDeviceService {

    PaginationResponse<AttendanceDeviceDTO> index(Sort.Direction direction, int page, int perPage);

    List<AttendanceDeviceDTO> getAllAttendanceDevices();

    AttendanceDeviceDTO store(AttendanceDeviceRequest request);

    AttendanceDeviceDTO show(Long attendanceDeviceId);

    AttendanceDeviceDTO update(Long attendanceDeviceId, AttendanceDeviceRequest request);

    void destroy(Long attendanceDeviceId);
}
