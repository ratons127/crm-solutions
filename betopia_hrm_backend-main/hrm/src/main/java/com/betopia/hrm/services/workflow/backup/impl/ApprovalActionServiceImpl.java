package com.betopia.hrm.services.workflow.backup.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ApprovalActionDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.ApprovalActionMapper;
import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.exceptions.ApprovalActionNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceStageNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowStageNotFound;
import com.betopia.hrm.domain.workflow.repository.ApprovalActionRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceStageRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowStageRepository;
import com.betopia.hrm.domain.workflow.request.ApprovalActionRequest;
import com.betopia.hrm.services.workflow.backup.ApprovalActionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class ApprovalActionServiceImpl implements ApprovalActionService {

    private final ApprovalActionRepository repository;
    private final ApprovalActionMapper mapper;
    private final WorkflowInstanceRepository instanceRepository;
    private final WorkflowInstanceStageRepository instanceStageRepository;
    private final WorkflowStageRepository stageRepository;

    public ApprovalActionServiceImpl(
            ApprovalActionRepository repository,
            ApprovalActionMapper mapper,
            WorkflowInstanceRepository instanceRepository,
            WorkflowInstanceStageRepository instanceStageRepository,
            WorkflowStageRepository stageRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.instanceRepository = instanceRepository;
        this.instanceStageRepository = instanceStageRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public PaginationResponse<ApprovalActionDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<ApprovalAction> approvalActionPage = repository.findAll(pageable);

        PaginationResponse<ApprovalActionDTO> response = new PaginationResponse<>();
        response.setData(approvalActionPage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All approval action fetched successfully");
        response.setLinks(Links.fromPage(approvalActionPage, "/approval-actions"));
        response.setMeta(Meta.fromPage(approvalActionPage, "/approval-actions"));

        return response;
    }

    @Override
    public List<ApprovalActionDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public ApprovalActionDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new ApprovalActionNotFound("Approval action not found with id: " + id)));
    }

    @Override
    public ApprovalActionDTO store(ApprovalActionRequest request) {
        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        WorkflowInstanceStage instanceStage = instanceStageRepository.findById(request.instanceStageId())
                .orElseThrow(() -> new WorkflowInstanceStageNotFound("Workflow instance stage not found with id:" + request.instanceStageId()));

        WorkflowStage workflowStage = stageRepository.findById(request.stageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + request.stageId()));

        ApprovalAction approvalAction=new ApprovalAction();
        approvalAction.setActionBy(request.actionBy());
        approvalAction.setActionType(request.actionType());
        approvalAction.setActionDate(request.actionDate());
        approvalAction.setComments(request.comments());
        approvalAction.setIpAddress(request.ipAddress());
        approvalAction.setUserAgent(request.userAgent());
        approvalAction.setInstance(instance);
        approvalAction.setInstanceStage(instanceStage);
        approvalAction.setStage(workflowStage);

        return mapper.toDTO(repository.save(approvalAction));
    }

    @Override
    public ApprovalActionDTO update(Long id, ApprovalActionRequest request) {
        ApprovalAction approvalAction = repository.findById(id)
                .orElseThrow(() -> new ApprovalActionNotFound("Approval action not found with id:" + id));

        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        WorkflowInstanceStage instanceStage = instanceStageRepository.findById(request.instanceStageId())
                .orElseThrow(() -> new WorkflowInstanceStageNotFound("Workflow instance stage not found with id:" + request.instanceStageId()));

        WorkflowStage workflowStage = stageRepository.findById(request.stageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + request.stageId()));

        updateIfNotNull(request.actionBy(), approvalAction::setActionBy);
        updateIfNotNull(request.actionType(), approvalAction::setActionType);
        updateIfNotNull(request.actionDate(), approvalAction::setActionDate);
        updateIfNotNull(request.comments(), approvalAction::setComments);
        updateIfNotNull(request.ipAddress(), approvalAction::setIpAddress);
        updateIfNotNull(request.userAgent(), approvalAction::setUserAgent);
        approvalAction.setInstance(instance);
        approvalAction.setInstanceStage(instanceStage);
        approvalAction.setStage(workflowStage);

        return mapper.toDTO(repository.save(approvalAction));
    }

    @Override
    public void delete(Long id) {
        ApprovalAction approvalAction = repository.findById(id)
                .orElseThrow(() -> new ApprovalActionNotFound("Approval action not found with id:" + id));

        repository.delete(approvalAction);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
