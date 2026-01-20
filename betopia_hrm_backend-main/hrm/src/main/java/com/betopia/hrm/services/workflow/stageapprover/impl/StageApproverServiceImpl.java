package com.betopia.hrm.services.workflow.stageapprover.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.StageApproverDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.StageApproverMapper;
import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.exceptions.StageApproverNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowStageNotFound;
import com.betopia.hrm.domain.workflow.repository.StageApproverRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowStageRepository;
import com.betopia.hrm.domain.workflow.request.StageApproverRequest;
import com.betopia.hrm.services.workflow.stageapprover.StageApproverService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class StageApproverServiceImpl implements StageApproverService {

    private final StageApproverRepository repository;
    private final StageApproverMapper mapper;
    private final WorkflowStageRepository stageRepository;

    public StageApproverServiceImpl(
            StageApproverRepository repository,
            StageApproverMapper mapper,
            WorkflowStageRepository stageRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.stageRepository = stageRepository;
    }

    @Override
    public PaginationResponse<StageApproverDTO> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<StageApprover> stageApproverPage = repository.findAll(pageable);

        PaginationResponse<StageApproverDTO> response = new PaginationResponse<>();
        response.setData(stageApproverPage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Stage approver fetched successfully");
        response.setLinks(Links.fromPage(stageApproverPage, "/stage-approvers"));
        response.setMeta(Meta.fromPage(stageApproverPage, "/stage-approvers"));

        return response;
    }

    @Override
    public List<StageApproverDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public StageApproverDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new StageApproverNotFound("Stage approver not found with id: " + id)));
    }

    @Override
    public StageApproverDTO store(StageApproverRequest request) {
        WorkflowStage workflowStage = stageRepository.findById(request.stageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + request.stageId()));

        StageApprover stageApprover = new StageApprover();
        stageApprover.setUserId(request.userId());
        stageApprover.setRoleId(request.roleId());
        stageApprover.setDepartmentId(request.departmentId());
        stageApprover.setApproverType(request.approverType());
        stageApprover.setSequenceOrder(request.sequenceOrder());
        stageApprover.setStatus(request.status());
        stageApprover.setStage(workflowStage);

        return mapper.toDTO(repository.save(stageApprover));
    }

    @Override
    public StageApproverDTO update(Long id, StageApproverRequest request) {
        StageApprover stageApprover = repository.findById(id)
                .orElseThrow(() -> new StageApproverNotFound("Stage approver not found with id:" + id));

        WorkflowStage workflowStage = stageRepository.findById(request.stageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + request.stageId()));

        updateIfNotNull(request.userId(), stageApprover::setUserId);
        updateIfNotNull(request.roleId(), stageApprover::setRoleId);
        updateIfNotNull(request.departmentId(), stageApprover::setDepartmentId);
        updateIfNotNull(request.approverType(), stageApprover::setApproverType);
        updateIfNotNull(request.sequenceOrder(), stageApprover::setSequenceOrder);
        updateIfNotNull(request.status(), stageApprover::setStatus);
        stageApprover.setStage(workflowStage);

        return mapper.toDTO(repository.save(stageApprover));
    }

    @Override
    public void delete(Long id) {
        StageApprover stageApprover = repository.findById(id)
                .orElseThrow(() -> new StageApproverNotFound("Stage approver not found with id:" + id));

        repository.delete(stageApprover);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
