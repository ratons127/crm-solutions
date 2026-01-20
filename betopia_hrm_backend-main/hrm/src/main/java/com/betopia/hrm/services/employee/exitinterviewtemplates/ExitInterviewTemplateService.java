package com.betopia.hrm.services.employee.exitinterviewtemplates;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitInterviewTemplatesDTO;
import com.betopia.hrm.domain.employee.request.ExitInterviewTemplatesRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ExitInterviewTemplateService {

    PaginationResponse<ExitInterviewTemplatesDTO> index(Sort.Direction direction, int page, int perPage);

    List<ExitInterviewTemplatesDTO> getAll();

    ExitInterviewTemplatesDTO store(ExitInterviewTemplatesRequest request);

    ExitInterviewTemplatesDTO show(Long id);

    ExitInterviewTemplatesDTO update(Long id,  ExitInterviewTemplatesRequest request);

    void destroy(Long id);
}
