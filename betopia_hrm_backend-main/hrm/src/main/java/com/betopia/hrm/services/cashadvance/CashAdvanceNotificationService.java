package com.betopia.hrm.services.cashadvance;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceApproval;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceApprovalRequest;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceNotificationDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CashAdvanceNotificationService {

    PaginationResponse<CashAdvanceNotificationDTO> index(Sort.Direction direction, int page, int perPage);

    List<CashAdvanceNotificationDTO> getAllCashAdvanceNotification();

    CashAdvanceNotificationDTO show(Long id);

    CashAdvanceNotificationDTO updateStatus(CashAdvanceApprovalRequest request);


    CashAdvanceNotificationDTO createInitialNotification(CashAdvanceApproval approval);

    String sendNotificationMessageByCashAdvanceRequest(Long id);

    String sendNotificationAfterApproval(Long cashAdvanceRequestId);
}
