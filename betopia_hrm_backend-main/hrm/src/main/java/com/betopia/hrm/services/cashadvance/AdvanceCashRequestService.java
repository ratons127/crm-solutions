package com.betopia.hrm.services.cashadvance;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import com.betopia.hrm.domain.cashadvance.model.AdvanceRequestDTO;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashRequestRequest;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashRequestDTO;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

public interface AdvanceCashRequestService {

    PaginationResponse<AdvanceCashRequestDTO> index(Sort.Direction direction, int page, int perPage);

    List<AdvanceCashRequestDTO> getAllAdvanceCashRequest();

    AdvanceCashRequestDTO store(AdvanceCashRequestRequest request);

    AdvanceCashRequestDTO show(Long advanceCashRequestId);

    AdvanceCashRequestDTO update(Long advanceCashRequestId, AdvanceCashRequestRequest request);

    void delete(Long advanceCashRequestId);

    AdvanceRequestDTO getAdvanceCashRequestByEmployee(BigDecimal requestedAmount);
}
