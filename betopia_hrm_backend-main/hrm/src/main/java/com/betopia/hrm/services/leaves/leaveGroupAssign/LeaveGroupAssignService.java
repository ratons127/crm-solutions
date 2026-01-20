package com.betopia.hrm.services.leaves.leaveGroupAssign;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveGroupAssignDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.request.LeaveGroupAssignRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface LeaveGroupAssignService {

    PaginationResponse<LeaveGroupAssignDTO> index(Sort.Direction direction, int page, int perPage);

    List<LeaveGroupAssignDTO> getAllLeaveGroupAssigns(Long leaveTypeId);

    LeaveGroupAssignDTO store(LeaveGroupAssignRequest request);

    LeaveGroupAssignDTO show(Long leaveGroupAssignId);

    LeaveGroupAssignDTO update(Long leaveGroupAssignId, LeaveGroupAssignRequest request);

    void destroy(Long leaveGroupAssignId);

    List<LeaveGroupAssign> findApplicableLeaveGroupAssign(Employee employee);

    LeaveGroupAssign findApplicableLeaveGroupAssignByEmployee(Employee employee, LeaveType leaveType);
}
