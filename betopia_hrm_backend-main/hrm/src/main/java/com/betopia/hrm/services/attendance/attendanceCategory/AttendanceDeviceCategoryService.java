package com.betopia.hrm.services.attendance.attendanceCategory;

import com.betopia.hrm.domain.attendance.request.AttendanceDeviceCategoryRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceCategoryDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AttendanceDeviceCategoryService {

    PaginationResponse<AttendanceDeviceCategoryDTO> index(Sort.Direction direction, int page, int perPage);

    List<AttendanceDeviceCategoryDTO> getAllAttendanceDeviceCategories();

    AttendanceDeviceCategoryDTO store(AttendanceDeviceCategoryRequest request);

    AttendanceDeviceCategoryDTO show(Long attendanceDeviceCategoryId);

    AttendanceDeviceCategoryDTO update(Long attendanceDeviceCategoryId, AttendanceDeviceCategoryRequest request);

    void destroy(Long attendanceDeviceCategoryId);
}
