package com.betopia.hrm.services.attendance.shiftEmployeeRotation;

import com.betopia.hrm.domain.attendance.request.ShiftEmployeeRotationRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftEmployeeRotationDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ShiftEmployeeRotationService {
    PaginationResponse<ShiftEmployeeRotationDTO> index(Sort.Direction direction, int page, int perPage);

    List<ShiftEmployeeRotationDTO> getAllShiftEmployeeRotation();

    ShiftEmployeeRotationDTO store(ShiftEmployeeRotationRequest request);

    ShiftEmployeeRotationDTO show(Long shiftEmployeeRotationId);

    ShiftEmployeeRotationDTO update(Long shiftEmployeeRotationId, ShiftEmployeeRotationRequest request);

    void destroy(Long shiftEmployeeRotationId);
}
