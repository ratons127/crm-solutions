package com.betopia.hrm.services.leaves.leaveCategory;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveCategory;
import com.betopia.hrm.domain.leave.request.LeaveCategoryRequest;
import com.betopia.hrm.services.base.BaseService;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LeaveCategoryService extends BaseService<LeaveCategory, LeaveCategoryRequest, Long> {

    List<LeaveCategory> importAndSave(MultipartFile file) throws Exception;

    byte[] export(boolean type, String format) throws Exception;
}
