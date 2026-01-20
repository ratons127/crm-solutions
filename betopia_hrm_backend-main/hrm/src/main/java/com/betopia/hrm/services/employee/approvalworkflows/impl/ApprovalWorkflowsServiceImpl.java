package com.betopia.hrm.services.employee.approvalworkflows.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.ApprovalWorkflows;
import com.betopia.hrm.domain.employee.exception.actingassignments.ActingAssignmentsNotFoundException;
import com.betopia.hrm.domain.employee.exception.approvalworkflows.ApprovalWorkflowsNotFoundException;
import com.betopia.hrm.domain.employee.exception.approvalworkflows.ApprovalWorkflowsNotFoundException;
import com.betopia.hrm.domain.employee.repository.ApprovalWorkflowsRepository;
import com.betopia.hrm.domain.employee.request.ApprovalWorkflowsRequest;
import com.betopia.hrm.domain.users.entity.Role;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.role.RoleNotFoundException;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.RoleRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.approvalworkflows.ApprovalWorkflowsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalWorkflowsServiceImpl implements ApprovalWorkflowsService {

    private final ApprovalWorkflowsRepository approvalWorkflowsRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public ApprovalWorkflowsServiceImpl(ApprovalWorkflowsRepository approvalWorkflowsRepository,
                                        UserRepository userRepository, RoleRepository roleRepository) {
        this.approvalWorkflowsRepository = approvalWorkflowsRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public PaginationResponse<ApprovalWorkflows> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<ApprovalWorkflows> approvalWorkflowsPage = approvalWorkflowsRepository.findAll(pageable);
        List<ApprovalWorkflows> approvalWorkflows = approvalWorkflowsPage.getContent();

        PaginationResponse<ApprovalWorkflows> response = new PaginationResponse<>();
        response.setData(approvalWorkflows);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Approval Workflows fetched successfully");

        Links links = Links.fromPage(approvalWorkflowsPage, "/approval-workflows");
        response.setLinks(links);

        Meta meta = Meta.fromPage(approvalWorkflowsPage, "/approval-workflows");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ApprovalWorkflows> getAll() {
        return approvalWorkflowsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public ApprovalWorkflows store(ApprovalWorkflowsRequest request) {
        Role role = roleRepository.findById(request.approverRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Approver Role not found with id: " + request.approverRoleId()));

        User approver = userRepository.findById(request.approverId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + request.approverId()));

        ApprovalWorkflows approvalWorkflows = new ApprovalWorkflows();
        approvalWorkflows.setModule(request.module());
        approvalWorkflows.setLevel(request.level());
        approvalWorkflows.setApproverRole(role);
        approvalWorkflows.setApprover(approver);
        approvalWorkflows.setSlaHours(request.slaHours());
        approvalWorkflows.setEscalationTo(role);
        approvalWorkflows.setStatus(request.status());

        return approvalWorkflowsRepository.save(approvalWorkflows);
    }

    @Override
    public ApprovalWorkflows show(Long id) {
        return approvalWorkflowsRepository.findById(id)
                .orElseThrow(() -> new ActingAssignmentsNotFoundException("Approval Workflows not found with id: " + id));
    }

    @Override
    public ApprovalWorkflows update(Long id, ApprovalWorkflowsRequest request) {
        ApprovalWorkflows approvalWorkflows = approvalWorkflowsRepository.findById(id)
                .orElseThrow(() -> new ApprovalWorkflowsNotFoundException("Approval Workflows not found with id: " + id));

        approvalWorkflows.setApprover(request.approverId() != null ? userRepository.
                findById(request.approverId()).orElse(null) : approvalWorkflows.getApprover());

        approvalWorkflows.setApproverRole(request.approverRoleId()!= null ? roleRepository.
                findById(request.approverRoleId()).orElse(null) : approvalWorkflows.getApproverRole());

        approvalWorkflows.setEscalationTo(request.escalationToRoleId()!= null ? roleRepository.
                findById(request.escalationToRoleId()).orElse(null) : approvalWorkflows.getEscalationTo());

        approvalWorkflows.setModule(request.module() != null ? request.module() : approvalWorkflows.getModule());
        approvalWorkflows.setLevel(request.level() != null ? request.level() : approvalWorkflows.getLevel());
        approvalWorkflows.setSlaHours(request.slaHours() != null ? request.slaHours() : approvalWorkflows.getSlaHours());
        approvalWorkflows.setStatus(request.status() != null ? request.status() : approvalWorkflows.getStatus());
        approvalWorkflows.setLastModifiedDate(LocalDateTime.now());

        return approvalWorkflowsRepository.save(approvalWorkflows);
    }

    @Override
    public void destroy(Long id) {
        ApprovalWorkflows approvalWorkflows = approvalWorkflowsRepository.findById(id)
                .orElseThrow(() -> new ApprovalWorkflowsNotFoundException("Approval Workflows not found with id: " + id));
        approvalWorkflowsRepository.delete(approvalWorkflows);
    }
}
