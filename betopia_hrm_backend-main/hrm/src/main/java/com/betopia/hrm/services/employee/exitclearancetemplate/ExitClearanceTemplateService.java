package com.betopia.hrm.services.employee.exitclearancetemplate;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitClearanceTemplateDTO;
import com.betopia.hrm.domain.employee.request.ExitClearanceTemplateRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ExitClearanceTemplateService {

    PaginationResponse<ExitClearanceTemplateDTO> index(Sort.Direction direction, int page, int perPage);

    List<ExitClearanceTemplateDTO> getAll();

    ExitClearanceTemplateDTO store(ExitClearanceTemplateRequest request);

    ExitClearanceTemplateDTO show(Long id);

    ExitClearanceTemplateDTO update(Long id,  ExitClearanceTemplateRequest request);

    void destroy(Long id);
}
