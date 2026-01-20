package com.betopia.hrm.services.attendance.shift;

import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.request.ShiftRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.services.base.BaseService;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ShiftService {

    PaginationResponse<ShiftDTO> index(Sort.Direction direction, int page, int perPage);

    List<ShiftDTO> getAllShift();

    ShiftDTO store(ShiftRequest request);

    ShiftDTO show(Long shiftId);

    ShiftDTO update(Long shiftId, ShiftRequest request);

    void destroy(Long shiftId);
}
