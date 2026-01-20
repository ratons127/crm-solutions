package com.betopia.hrm.services.cashadvance.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceApproval;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceNotifications;
import com.betopia.hrm.domain.cashadvance.enums.CashAdvanceStatus;
import com.betopia.hrm.domain.cashadvance.repository.CashAdvanceApprovalRepository;
import com.betopia.hrm.domain.cashadvance.repository.CashAdvanceNotificationRepository;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceApprovalRequest;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceNotificationDTO;
import com.betopia.hrm.domain.dto.cashadvance.mapper.CashAdvanceNotificationMapper;
import com.betopia.hrm.services.cashadvance.CashAdvanceNotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashAdvanceNotificationServiceImpl implements CashAdvanceNotificationService {

    private final CashAdvanceApprovalRepository cashAdvanceApprovalRepository;
    private final CashAdvanceNotificationRepository cashAdvanceNotificationRepository;
    private final CashAdvanceNotificationMapper cashAdvanceNotificationMapper;

    public CashAdvanceNotificationServiceImpl(CashAdvanceApprovalRepository cashAdvanceApprovalRepository, CashAdvanceNotificationRepository cashAdvanceNotificationRepository, CashAdvanceNotificationMapper cashAdvanceNotificationMapper) {
        this.cashAdvanceApprovalRepository = cashAdvanceApprovalRepository;
        this.cashAdvanceNotificationRepository = cashAdvanceNotificationRepository;
        this.cashAdvanceNotificationMapper = cashAdvanceNotificationMapper;
    }

    @Override
    public PaginationResponse<CashAdvanceNotificationDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<CashAdvanceNotifications> cashAdvanceNotificationsPage = cashAdvanceNotificationRepository.findAll(pageable);

        List<CashAdvanceNotifications> cashAdvanceNotifications = cashAdvanceNotificationsPage.getContent();
        List<CashAdvanceNotificationDTO> cashAdvanceNotificationDTOs = cashAdvanceNotificationMapper.toDTOList(cashAdvanceNotifications);

        PaginationResponse<CashAdvanceNotificationDTO> response = new PaginationResponse<>();

        response.setData(cashAdvanceNotificationDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All CashAdvanceNotifications fetch successful");

        Links links = Links.fromPage(cashAdvanceNotificationsPage, "/cashAdvanceNotifications");
        response.setLinks(links);

        Meta meta = Meta.fromPage(cashAdvanceNotificationsPage, "/cashAdvanceNotifications");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<CashAdvanceNotificationDTO> getAllCashAdvanceNotification() {
        List<CashAdvanceNotifications> pendingNotifications =
                cashAdvanceNotificationRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                        .stream()
                        .filter(p -> CashAdvanceStatus.PENDING.name().equals(p.getNotificationStatus()))
                        .toList();

        return cashAdvanceNotificationMapper.toDTOList(pendingNotifications);
    }


    @Override
    public CashAdvanceNotificationDTO show(Long cashAdvanceNotificationsId) {
        CashAdvanceNotifications cashAdvanceNotifications = cashAdvanceNotificationRepository.findById(cashAdvanceNotificationsId)
                .orElseThrow(() -> new RuntimeException("CashAdvanceNotifications not found with id: " + cashAdvanceNotificationsId));

        return cashAdvanceNotificationMapper.toDTO(cashAdvanceNotifications);
    }

    @Override
    public CashAdvanceNotificationDTO updateStatus(CashAdvanceApprovalRequest request) {
        CashAdvanceNotifications cashAdvanceNotifications = cashAdvanceNotificationRepository.findById(request.cashAdvanceApprovalId())
                .orElseThrow(() -> new RuntimeException("CashAdvanceNotifications not found with id: " + request.cashAdvanceApprovalId()));

        cashAdvanceNotifications.setNotificationStatus(request.cashAdvanceStatus());
        cashAdvanceNotifications.setMessage(request.remarks());
        return cashAdvanceNotificationMapper.toDTO(cashAdvanceNotificationRepository.save(cashAdvanceNotifications));
    }


    @Override
    public CashAdvanceNotificationDTO createInitialNotification(CashAdvanceApproval cashAdvanceApproval) {

        CashAdvanceApproval approvals = cashAdvanceApprovalRepository.findById(cashAdvanceApproval.getId())
                .orElseThrow(() -> new RuntimeException("CashAdvanceApproval not found with id: " + cashAdvanceApproval.getId()));

        CashAdvanceNotifications cashAdvanceNotifications = new CashAdvanceNotifications();
        cashAdvanceNotifications.setCashAdvanceApproval(approvals);
        cashAdvanceNotifications.setSender(approvals.getEmployee().getId());
        cashAdvanceNotifications.setRecipient(approvals.getApproverId());
        cashAdvanceNotifications.setType("CASH_ADVANCE_REQUEST_SUBMITTED");
        cashAdvanceNotifications.setMessage("New cash advance request pending for approval.Need your approval");
        cashAdvanceNotifications.setNotificationStatus(CashAdvanceStatus.PENDING.name());
        return cashAdvanceNotificationMapper.toDTO(cashAdvanceNotificationRepository.save(cashAdvanceNotifications));
    }

    @Override
    public String sendNotificationMessageByCashAdvanceRequest(Long id) {
        return "";
    }

    @Override
    public String sendNotificationAfterApproval(Long cashAdvanceRequestId) {
        return "";
    }
}
