package com.betopia.hrm.services.attendance.shiftRotationPattern;

import com.betopia.hrm.domain.attendance.request.ShiftRotationPatternsRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftRotationPatternDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ShiftRotationPatternService {

    PaginationResponse<ShiftRotationPatternDTO> index(Sort.Direction direction, int page, int perPage);

    List<ShiftRotationPatternDTO> getAllShiftRotationPattern();

    ShiftRotationPatternDTO store(ShiftRotationPatternsRequest shiftRotationPatternsRequest);

    ShiftRotationPatternDTO show(Long shiftRotationPatternId);

    ShiftRotationPatternDTO update(Long shiftRotationPatternId, ShiftRotationPatternsRequest shiftRotationPatternsRequest);

    void destroy(Long shiftRotationPatternId);
}
