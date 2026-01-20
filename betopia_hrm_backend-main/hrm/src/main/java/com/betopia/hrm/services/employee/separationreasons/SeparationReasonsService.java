package com.betopia.hrm.services.employee.separationreasons;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationReasonsDTO;
import com.betopia.hrm.domain.employee.request.SeparationReasonsRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SeparationReasonsService {

    PaginationResponse<SeparationReasonsDTO> index(Sort.Direction direction, int page, int perPage);

    List<SeparationReasonsDTO> getAll();

    SeparationReasonsDTO show(Long id);

    SeparationReasonsDTO update(Long id, SeparationReasonsRequest request);

    void destroy(Long id);

    SeparationReasonsDTO store(SeparationReasonsRequest request);
}
