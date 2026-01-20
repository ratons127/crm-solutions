package com.betopia.hrm.services.cashadvance;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashConfig;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashConfigRequest;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashConfigDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AdvanceCashConfigService {

    PaginationResponse<AdvanceCashConfigDTO> index(Sort.Direction direction, int page, int perPage);

    List<AdvanceCashConfigDTO> getAllAdvanceCashConfig();

    AdvanceCashConfigDTO store(AdvanceCashConfigRequest request);

    AdvanceCashConfigDTO show(Long advanceCashConfigId);

    AdvanceCashConfigDTO update(Long advanceCashConfigId, AdvanceCashConfigRequest request);

    void delete(Long advanceCashConfigId);

}
