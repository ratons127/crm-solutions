package com.betopia.hrm.services.leaves.leaverequest;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveRequestDTO;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.request.LeaveRequestCreateRequest;
import com.betopia.hrm.webapp.util.SmsResponse;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LeaveRequestService {

    PaginationResponse<LeaveRequestDTO> index(Sort.Direction direction, int page, int perPage,Long userId,String keyword);

    List<LeaveRequestDTO> getAllLeaveRequests(Long userId);

    LeaveRequest getLeaveRequestById(Long id);

    LeaveRequestDTO store(LeaveRequestCreateRequest request, List<MultipartFile> files);

    LeaveRequest updateLeaveRequest(Long id, LeaveRequestCreateRequest request);

    void deleteLeaveRequest(Long id);

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    SmsResponse sendLeaveApprovalNotification(Long leaveRequestId);

    LeaveRequestDTO update(Long id, LeaveRequestCreateRequest request, List<MultipartFile> files);
}
