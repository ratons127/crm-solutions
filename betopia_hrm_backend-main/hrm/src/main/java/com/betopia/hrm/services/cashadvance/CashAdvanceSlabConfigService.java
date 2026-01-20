package com.betopia.hrm.services.cashadvance;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfig;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceSlabConfigRequest;
import com.betopia.hrm.domain.cashadvance.request.UpdateCashAdvanceSlabConfigRequest;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import com.betopia.hrm.domain.dto.cashadvance.mapper.CashAdvanceSlabConfigMapper;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CashAdvanceSlabConfigService {

    PaginationResponse<CashAdvanceSlabConfigDTO> index(Sort.Direction direction, int page, int perPage);

    List<CashAdvanceSlabConfigDTO> getAllCashAdvanceSlabConfig();

    CashAdvanceSlabConfigDTO store(CashAdvanceSlabConfigRequest request);

    CashAdvanceSlabConfigDTO show(Long cashAdvanceSlabConfigId);

    CashAdvanceSlabConfigDTO update(Long cashAdvanceSlabConfigId, UpdateCashAdvanceSlabConfigRequest request);

    void delete(Long cashAdvanceSlabConfigId);
}
