package com.betopia.hrm.services.cashadvance;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceApproval;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceApprovalRequest;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceApprovalDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CashAdvanceApprovalService {

    PaginationResponse<CashAdvanceApprovalDTO> index(Sort.Direction direction, int page, int perPage);

    List<CashAdvanceApprovalDTO> getAllCashAdvanceApproval();

    CashAdvanceApprovalDTO show(Long id);

    CashAdvanceApprovalDTO updateStatus(Long id, CashAdvanceApprovalRequest request);

    CashAdvanceApprovalDTO createInitialApproval(AdvanceCashRequest advanceCashRequest);

}
