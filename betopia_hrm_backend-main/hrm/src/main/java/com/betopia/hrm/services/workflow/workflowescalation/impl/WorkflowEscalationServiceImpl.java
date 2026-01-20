package com.betopia.hrm.services.workflow.workflowescalation.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowEscalationDTO;
import com.betopia.hrm.domain.dto.workflow.WorkflowNotificationDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.WorkflowEscalationMapper;
import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import com.betopia.hrm.domain.workflow.entity.WorkflowEscalation;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowNotification;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowEscalationNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceStageNotFound;
import com.betopia.hrm.domain.workflow.repository.WorkflowEscalationRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceStageRepository;
import com.betopia.hrm.domain.workflow.request.WorkflowEscalationRequest;
import com.betopia.hrm.services.workflow.workflowescalation.WorkflowEscalationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class WorkflowEscalationServiceImpl implements WorkflowEscalationService {

    private final WorkflowEscalationRepository repository;
    private final WorkflowEscalationMapper mapper;
    private final WorkflowInstanceRepository instanceRepository;
    private final WorkflowInstanceStageRepository instanceStageRepository;

    public WorkflowEscalationServiceImpl(
            WorkflowEscalationRepository repository,
            WorkflowEscalationMapper mapper,
            WorkflowInstanceRepository instanceRepository,
            WorkflowInstanceStageRepository instanceStageRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.instanceRepository = instanceRepository;
        this.instanceStageRepository = instanceStageRepository;
    }

    @Override
    public PaginationResponse<WorkflowEscalationDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<WorkflowEscalation> workflowEscalationPage = repository.findAll(pageable);

        PaginationResponse<WorkflowEscalationDTO> response = new PaginationResponse<>();
        response.setData(workflowEscalationPage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workflow escalations fetched successfully");
        response.setLinks(Links.fromPage(workflowEscalationPage, "/workflow-escalations"));
        response.setMeta(Meta.fromPage(workflowEscalationPage, "/workflow-escalations"));

        return response;
    }

    @Override
    public List<WorkflowEscalationDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WorkflowEscalationDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new WorkflowEscalationNotFound("Workflow escalation not found with id: " + id)));
    }

    @Override
    public WorkflowEscalationDTO store(WorkflowEscalationRequest request) {
        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        WorkflowInstanceStage instanceStage = instanceStageRepository.findById(request.instanceStageId())
                .orElseThrow(() -> new WorkflowInstanceStageNotFound("Workflow instance stage not found with id:" + request.instanceStageId()));

        WorkflowEscalation escalation=new WorkflowEscalation();
        escalation.setEscalatedFrom(request.escalatedFrom());
        escalation.setEscalatedTo(request.escalatedTo());
        escalation.setEscalationReason(request.escalationReason());
        escalation.setEscalatedAt(request.escalatedAt());
        escalation.setResolvedAt(request.resolvedAt());
        escalation.setInstance(instance);
        escalation.setInstanceStage(instanceStage);

        return mapper.toDTO(repository.save(escalation));
    }

    @Override
    public WorkflowEscalationDTO update(Long id, WorkflowEscalationRequest request) {
        WorkflowEscalation escalation = repository.findById(id)
                .orElseThrow(() -> new WorkflowEscalationNotFound("Workflow escalation not found with id:" + id));

        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        WorkflowInstanceStage instanceStage = instanceStageRepository.findById(request.instanceStageId())
                .orElseThrow(() -> new WorkflowInstanceStageNotFound("Workflow instance stage not found with id:" + request.instanceStageId()));

        updateIfNotNull(request.escalatedFrom(), escalation::setEscalatedFrom);
        updateIfNotNull(request.escalatedTo(), escalation::setEscalatedTo);
        updateIfNotNull(request.escalationReason(), escalation::setEscalationReason);
        updateIfNotNull(request.escalatedAt(), escalation::setEscalatedAt);
        updateIfNotNull(request.resolvedAt(), escalation::setResolvedAt);
        escalation.setInstance(instance);
        escalation.setInstanceStage(instanceStage);

        return mapper.toDTO(repository.save(escalation));
    }

    @Override
    public void delete(Long id) {
        WorkflowEscalation escalation = repository.findById(id)
                .orElseThrow(() -> new WorkflowEscalationNotFound("Workflow escalation not found with id:" + id));

        repository.delete(escalation);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
