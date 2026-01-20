package com.betopia.hrm.services.workflow.module;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ModuleDTO;
import com.betopia.hrm.domain.workflow.request.ModuleRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ModuleService {

    PaginationResponse<ModuleDTO> index(Sort.Direction direction, int page, int perPage);

    List<ModuleDTO> getAll();

    ModuleDTO getById(Long id);

    ModuleDTO store(ModuleRequest request);

    ModuleDTO update(Long id, ModuleRequest request);

    void delete(Long id);
}
