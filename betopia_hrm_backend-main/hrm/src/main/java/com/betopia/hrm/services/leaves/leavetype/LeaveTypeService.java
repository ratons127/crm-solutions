package com.betopia.hrm.services.leaves.leavetype;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.request.LeaveTypeRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LeaveTypeService {

    PaginationResponse<LeaveType> index(Sort.Direction direction, int page, int perPage);

    List<LeaveType> getAllLeaveTypes();

    List<LeaveType> getAllLeaveTypeByStatus(Boolean status);

    LeaveType getLeaveTypeById(Long id);

    LeaveType store(LeaveTypeRequest request);

    LeaveType updateLeaveType(Long id, LeaveTypeRequest request);

    void deleteLeaveType(Long id);

    List<LeaveType> importAndSave(MultipartFile file) throws Exception;

    byte[] export(Boolean type, String format) throws Exception;
}
