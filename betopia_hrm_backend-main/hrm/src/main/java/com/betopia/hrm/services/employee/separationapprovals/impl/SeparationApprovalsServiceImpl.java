package com.betopia.hrm.services.employee.separationapprovals.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeSeparations;
import com.betopia.hrm.domain.employee.entity.SeparationApprovals;
import com.betopia.hrm.domain.employee.exception.employeeseparations.EmployeeSeparationsNotFound;
import com.betopia.hrm.domain.employee.exception.separationapprovals.SeparationApprovalsNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.SeparationApprovalsRepository;
import com.betopia.hrm.domain.employee.request.SeparationApprovalsRequest;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.separationapprovals.SeparationApprovalsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeparationApprovalsServiceImpl implements SeparationApprovalsService {

    public final SeparationApprovalsRepository separationApprovalsRepository;
    public final EmployeeSeparationsRepository employeeSeparationsRepository;
    public final UserRepository userRepository;

    public EmployeeSeparationsRepository getEmployeeSeparationsRepository() {
        return employeeSeparationsRepository;
    }

    public SeparationApprovalsServiceImpl(SeparationApprovalsRepository separationApprovalsRepository,
                                          EmployeeSeparationsRepository employeeSeparationsRepository,UserRepository userRepository){
        this.separationApprovalsRepository = separationApprovalsRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<SeparationApprovals> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<SeparationApprovals> separationApprovalsPage = separationApprovalsRepository.findAll(pageable);
        List<SeparationApprovals> separationApprovals = separationApprovalsPage.getContent();

        PaginationResponse<SeparationApprovals> response = new PaginationResponse<>();
        response.setData(separationApprovals);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Separations Approval fetched successfully");

        Links links = Links.fromPage(separationApprovalsPage, "/separation-approval");
        response.setLinks(links);

        Meta meta = Meta.fromPage(separationApprovalsPage, "/separation-approval");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<SeparationApprovals> getAll() {
        return separationApprovalsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public SeparationApprovals store(SeparationApprovalsRequest request) {
        EmployeeSeparations employeeSeparations = employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found with id: " + request.separationId()));

        User approver = userRepository.findById(request.approverId())
                .orElseThrow(() -> new UsernameNotFoundException("Approver not found with id: " + request.approverId()));

        // Create new EmployeeSeparations entity
        SeparationApprovals separation = new SeparationApprovals();
        separation.setSeparation(employeeSeparations);
        separation.setApprovalLevel(request.approvalLevel());
        separation.setApprover(approver);
        separation.setAction(request.action());
        separation.setRemarks(request.remarks());
        separation.setActionDate(request.actionDate());
        separation.setSlaDeadline(request.slaDeadline());
        separation.setIsOverdue(request.isOverdue());
        separation.setSequenceOrder(request.sequenceOrder());

        return separationApprovalsRepository.save(separation);
    }

    @Override
    public SeparationApprovals show(Long id) {
        return separationApprovalsRepository.findById(id)
                .orElseThrow(() -> new SeparationApprovalsNotFound("Separation approval not found with id: " + id));
    }

    @Override
    public SeparationApprovals update(Long id, SeparationApprovalsRequest request) {
        SeparationApprovals separation = separationApprovalsRepository.findById(id)
                .orElseThrow(() -> new EmployeeSeparationsNotFound("Separation approval not found with id: " + id));

        separation.setSeparation(request.separationId()!= null ? employeeSeparationsRepository.
                findById(request.separationId()).orElse(null) : separation.getSeparation());

        separation.setApprover(request.approverId()!= null ? userRepository.
                findById(request.approverId()).orElse(null) : separation.getApprover());

        separation.setApprovalLevel(request.approvalLevel() != null ? request.approvalLevel() : separation.getApprovalLevel());
        separation.setAction(request.action() != null ? request.action() : separation.getAction());
        separation.setRemarks(request.remarks() != null ? request.remarks() : separation.getRemarks());
        separation.setActionDate(request.actionDate() != null ? request.actionDate() : separation.getActionDate());
        separation.setSlaDeadline(request.slaDeadline() != null ? request.slaDeadline() : separation.getSlaDeadline());
        separation.setIsOverdue(request.isOverdue() != null ? request.isOverdue() : separation.getIsOverdue());
        separation.setSequenceOrder(request.sequenceOrder() != null ? request.sequenceOrder() : separation.getSequenceOrder());

        return separationApprovalsRepository.save(separation);    }

    @Override
    public void destroy(Long id) {
        SeparationApprovals separationApprovals = separationApprovalsRepository.findById(id)
                .orElseThrow(() -> new EmployeeSeparationsNotFound("Separation approval not found with id: " + id));
        separationApprovalsRepository.delete(separationApprovals);
    }
}
