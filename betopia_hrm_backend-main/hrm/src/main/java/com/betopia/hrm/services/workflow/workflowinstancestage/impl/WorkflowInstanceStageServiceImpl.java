package com.betopia.hrm.services.workflow.workflowinstancestage.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceStageDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.WorkflowInstanceStageMapper;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceStageNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowStageNotFound;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceStageRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowStageRepository;
import com.betopia.hrm.domain.workflow.request.WorkflowInstanceStageRequest;
import com.betopia.hrm.services.workflow.workflowinstancestage.WorkflowInstanceStageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class WorkflowInstanceStageServiceImpl implements WorkflowInstanceStageService {

    private final WorkflowInstanceStageRepository repository;
    private final WorkflowInstanceStageMapper mapper;
    private final WorkflowInstanceRepository instanceRepository;
    private final WorkflowStageRepository stageRepository;

    public WorkflowInstanceStageServiceImpl(
            WorkflowInstanceStageRepository repository,
            WorkflowInstanceStageMapper mapper,
            WorkflowInstanceRepository instanceRepository,
            WorkflowStageRepository stageRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.instanceRepository = instanceRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    public PaginationResponse<WorkflowInstanceStageDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<WorkflowInstanceStage> workflowInstanceStagePage = repository.findAll(pageable);

        PaginationResponse<WorkflowInstanceStageDTO> response = new PaginationResponse<>();
        response.setData(workflowInstanceStagePage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workflow instance stage fetched successfully");
        response.setLinks(Links.fromPage(workflowInstanceStagePage, "/workflow-instance-stages"));
        response.setMeta(Meta.fromPage(workflowInstanceStagePage, "/workflow-instance-stages"));

        return response;
    }

    @Override
    public List<WorkflowInstanceStageDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WorkflowInstanceStageDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new WorkflowInstanceStageNotFound("Workflow instance stage not found with id: " + id)));
    }

    @Override
    public WorkflowInstanceStageDTO store(WorkflowInstanceStageRequest request) {
        WorkflowInstance workflowInstance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance stage not found with id:" + request.instanceId()));

        WorkflowStage workflowStage = stageRepository.findById(request.stageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow instance stage not found with id:" + request.stageId()));

        WorkflowInstanceStage instanceStage = new WorkflowInstanceStage();
        instanceStage.setStageStatus(request.stageStatus());
        instanceStage.setAssignedTo(request.assignedTo());
        instanceStage.setAssignedAt(request.assignedAt());
        instanceStage.setStartedAt(request.startedAt());
        instanceStage.setCompletedAt(request.completedAt());
        instanceStage.setRemarks(request.remarks());
        instanceStage.setInstance(workflowInstance);
        instanceStage.setStage(workflowStage);

        return mapper.toDTO(repository.save(instanceStage));
    }

    @Override
    public WorkflowInstanceStageDTO update(Long id, WorkflowInstanceStageRequest request) {
        WorkflowInstanceStage instanceStage = repository.findById(id)
                .orElseThrow(() -> new WorkflowInstanceStageNotFound("Workflow instance stage not found with id:" + id));

        WorkflowInstance workflowInstance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        WorkflowStage workflowStage = stageRepository.findById(request.stageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + request.stageId()));

        updateIfNotNull(request.stageStatus(), instanceStage::setStageStatus);
        updateIfNotNull(request.assignedTo(), instanceStage::setAssignedTo);
        updateIfNotNull(request.assignedAt(), instanceStage::setAssignedAt);
        updateIfNotNull(request.startedAt(), instanceStage::setStartedAt);
        updateIfNotNull(request.completedAt(), instanceStage::setCompletedAt);
        updateIfNotNull(request.remarks(), instanceStage::setRemarks);
        instanceStage.setInstance(workflowInstance);
        instanceStage.setStage(workflowStage);

        return mapper.toDTO(repository.save(instanceStage));
    }

    @Override
    public void delete(Long id) {
        WorkflowInstanceStage instanceStage = repository.findById(id)
                .orElseThrow(() -> new WorkflowInstanceStageNotFound("Workflow instance stage not found with id:" + id));

        repository.delete(instanceStage);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
