package com.betopia.hrm.services.employee.exitinterview;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitInterviewDTO;
import com.betopia.hrm.domain.employee.request.ExitInterviewRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ExitInterviewService {

    PaginationResponse<ExitInterviewDTO> index(Sort.Direction direction, int page, int perPage);

    List<ExitInterviewDTO> getAll();

    ExitInterviewDTO store(ExitInterviewRequest request);

    ExitInterviewDTO show(Long id);

    ExitInterviewDTO update(Long id,  ExitInterviewRequest request);

    void destroy(Long id);

}
