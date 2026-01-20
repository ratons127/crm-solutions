package com.betopia.hrm.services.cashadvance.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceApproval;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceNotifications;
import com.betopia.hrm.domain.cashadvance.enums.CashAdvanceStatus;
import com.betopia.hrm.domain.cashadvance.exception.AdvanceCashRequestNotFoundException;
import com.betopia.hrm.domain.cashadvance.repository.AdvanceCashRequestRepository;
import com.betopia.hrm.domain.cashadvance.repository.CashAdvanceApprovalRepository;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceApprovalRequest;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceApprovalDTO;
import com.betopia.hrm.domain.dto.cashadvance.mapper.CashAdvanceApprovalMapper;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.enums.LeaveStatus;
import com.betopia.hrm.services.cashadvance.CashAdvanceApprovalService;
import com.betopia.hrm.services.cashadvance.CashAdvanceNotificationService;
import com.betopia.hrm.webapp.util.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CashAdvanceApprovalServiceImpl implements CashAdvanceApprovalService {

    private final EmployeeRepository employeeRepository;
    private final CashAdvanceApprovalMapper cashAdvanceApprovalMapper;
    private final AdvanceCashRequestRepository advanceCashRequestRepository;
    private final CashAdvanceApprovalRepository advanceAprovalRepository;
    private final CashAdvanceNotificationService cashAdvanceNotificationService;

    public CashAdvanceApprovalServiceImpl(EmployeeRepository employeeRepository, CashAdvanceApprovalMapper cashAdvanceApprovalMapper, AdvanceCashRequestRepository advanceCashRequestRepository, CashAdvanceApprovalRepository advanceAprovalRepository, CashAdvanceNotificationService cashAdvanceNotificationService) {
        this.employeeRepository = employeeRepository;
        this.cashAdvanceApprovalMapper = cashAdvanceApprovalMapper;
        this.advanceCashRequestRepository = advanceCashRequestRepository;
        this.advanceAprovalRepository = advanceAprovalRepository;
        this.cashAdvanceNotificationService = cashAdvanceNotificationService;
    }

    @Override
    public PaginationResponse<CashAdvanceApprovalDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<CashAdvanceApproval> cashAdvanceApprovalPage = advanceAprovalRepository.findAll(pageable);

        List<CashAdvanceApproval> cashAdvanceApproval = cashAdvanceApprovalPage.getContent();
        List<CashAdvanceApprovalDTO> cashAdvanceApprovalDTOs = cashAdvanceApprovalMapper.toDTOList(cashAdvanceApproval);

        PaginationResponse<CashAdvanceApprovalDTO> response = new PaginationResponse<>();

        response.setData(cashAdvanceApprovalDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All CashAdvanceApproval fetch successful");

        Links links = Links.fromPage(cashAdvanceApprovalPage, "/cashAdvanceApprovals");
        response.setLinks(links);

        Meta meta = Meta.fromPage(cashAdvanceApprovalPage, "/cashAdvanceApprovals");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<CashAdvanceApprovalDTO> getAllCashAdvanceApproval() {
        List<CashAdvanceApproval> pendingAdavnceApproval =
                advanceAprovalRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                        .stream()
                        .filter(p -> CashAdvanceStatus.PENDING.name().equals(p.getStatus())).toList();

        return cashAdvanceApprovalMapper.toDTOList(pendingAdavnceApproval);
    }

    @Override
    public CashAdvanceApprovalDTO show(Long cashAdvanceApprovalId) {
        CashAdvanceApproval cashAdvanceApproval = advanceAprovalRepository.findById(cashAdvanceApprovalId)
                .orElseThrow(() -> new RuntimeException("CashAdvanceApproval not found with id: " + cashAdvanceApprovalId));

        return cashAdvanceApprovalMapper.toDTO(cashAdvanceApproval);
    }

    @Override
    @Transactional
    public CashAdvanceApprovalDTO updateStatus(Long cashAdvanceApprovalId, CashAdvanceApprovalRequest request) {
        CashAdvanceApproval approval = advanceAprovalRepository.findById(cashAdvanceApprovalId)
                .orElseThrow(() -> new RuntimeException("CashAdvanceApproval not found with id: " + cashAdvanceApprovalId));

        approval.setStatus(request.cashAdvanceStatus());
        approval.setRemarks(request.remarks());
        approval.setActionDate(LocalDateTime.now());
        approval.setApproverId(Long.parseLong(AuthUtils.getCurrentUsername()));
        CashAdvanceApproval savedCashAdvanceApproval = advanceAprovalRepository.save(approval);

        cashAdvanceNotificationService.updateStatus(request);

        return cashAdvanceApprovalMapper.toDTO(savedCashAdvanceApproval);
    }


    @Override
    @Transactional
    public CashAdvanceApprovalDTO createInitialApproval(AdvanceCashRequest request) {
        Employee employee = employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream().filter(p->p.getEmployeeSerialId().equals(request.getEmployeeId())).findFirst()
                .orElseThrow(() -> new EmployeeNotFound("Employee not found with id: " + request.getEmployeeId()));

        // Get supervisor entity
        Long supervisorId = employee.getSupervisorId();
        if (supervisorId == null)
            throw new IllegalStateException("Employee " + employee.getId() + " does not have a supervisor assigned");

        AdvanceCashRequest advanceCashRequest = advanceCashRequestRepository.findById(request.getId())
                .orElseThrow(() -> new AdvanceCashRequestNotFoundException("AdvanceCashRequest not found with id: " + request.getId()));

        CashAdvanceApproval approval = new CashAdvanceApproval();
        approval.setEmployee(employee);
        approval.setAdvanceCashRequest(advanceCashRequest);
        approval.setApproverId(supervisorId);
        approval.setLevel(1);
        approval.setStatus(request.getStatus());

        CashAdvanceApproval CashAdvanceApproval= advanceAprovalRepository.save(approval);

        cashAdvanceNotificationService.createInitialNotification(CashAdvanceApproval);

        return cashAdvanceApprovalMapper.toDTO(CashAdvanceApproval);
    }

}
