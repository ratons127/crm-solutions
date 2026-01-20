package com.betopia.hrm.services.attendance.manualattendance;

import com.betopia.hrm.domain.attendance.request.ManualAttendanceRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ManualAttendanceDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ManualAttendanceService {

    PaginationResponse<ManualAttendanceDTO> index(Sort.Direction direction, int page, int perPage,
                                                  String keyword,Long userId);

    List<ManualAttendanceDTO> getAll(Long userId);

    ManualAttendanceDTO store(ManualAttendanceRequest request);

    ManualAttendanceDTO show(Long Id);

    ManualAttendanceDTO update(Long Id, ManualAttendanceRequest request);

    void destroy(Long Id);


}
