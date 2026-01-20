package com.betopia.hrm.services.employee.noticeperiodconfig;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.NoticePeriodConfigDTO;
import com.betopia.hrm.domain.employee.request.NoticePeriodConfigRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NoticePeriodConfigService{

    PaginationResponse<NoticePeriodConfigDTO> index(Sort.Direction direction, int page, int perPage);

    List<NoticePeriodConfigDTO> getAllNoticePeriodConfigs();

    NoticePeriodConfigDTO store(NoticePeriodConfigRequest request);

    NoticePeriodConfigDTO show(Long id);

    NoticePeriodConfigDTO update(Long id,  NoticePeriodConfigRequest request);

    void destroy(Long id);
}
