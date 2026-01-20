package com.betopia.hrm.services.attendance.shiftassignment;

import com.betopia.hrm.domain.attendance.request.ShiftAssignmentsRequest;
import com.betopia.hrm.domain.attendance.request.ShowShiftAssignRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftAssignmentsDTO;
import com.betopia.hrm.domain.dto.attendance.ShowShiftAssignDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ShiftAssignmentsService {

    PaginationResponse<ShiftAssignmentsDTO> index(Sort.Direction direction, int page, int perPage);

    List<ShiftAssignmentsDTO> getAll();

    ShiftAssignmentsDTO store(ShiftAssignmentsRequest request);

    ShiftAssignmentsDTO show(Long Id);

    ShiftAssignmentsDTO update(Long Id, ShiftAssignmentsRequest request);

    void destroy(Long Id);

    List<ShiftAssignmentsDTO> assignShiftToEmployees(ShiftAssignmentsRequest request);

    List<ShowShiftAssignDTO> getAllShiftAssignments();
}
