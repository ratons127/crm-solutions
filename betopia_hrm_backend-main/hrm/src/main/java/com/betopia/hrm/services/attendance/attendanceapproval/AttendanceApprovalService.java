package com.betopia.hrm.services.attendance.attendanceapproval;

import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.attendance.request.ApproveAttendanceStatusRequest;
import com.betopia.hrm.domain.attendance.request.AttendanceApprovalRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceApprovalDTO;
import com.betopia.hrm.domain.dto.leave.LeaveApprovalsDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AttendanceApprovalService {

    PaginationResponse<AttendanceApprovalDTO> index(Sort.Direction direction, int page, int perPage, String keyword);

    List<AttendanceApprovalDTO> getAllAttendanceApprovals();

    AttendanceApprovalDTO store(AttendanceApprovalRequest request);

    AttendanceApprovalDTO show(Long attendanceApprovalId);

    AttendanceApprovalDTO update(Long attendanceApprovalId, AttendanceApprovalRequest request);

    void destroy(Long attendanceApprovalId);

    AttendanceApprovalDTO createInitialAttendanceApproval(ManualAttendance manualAttendance);

    List<AttendanceApprovalDTO> updateAttendanceStatus(List<ApproveAttendanceStatusRequest> requests);

    List<AttendanceApprovalDTO> getApprovalsForSupervisor();
}
