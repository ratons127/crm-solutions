package com.betopia.hrm.services.workflow.workflowstage.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowStageDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.WorkflowStageMapper;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowTemplate;
import com.betopia.hrm.domain.workflow.exceptions.ModuleNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowStageNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowTemplateNotFound;
import com.betopia.hrm.domain.workflow.repository.WorkflowStageRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowTemplateRepository;
import com.betopia.hrm.domain.workflow.request.WorkflowStageRequest;
import com.betopia.hrm.services.workflow.workflowstage.WorkflowStageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class WorkflowStageServiceImpl implements WorkflowStageService {

    private final WorkflowStageRepository repository;
    private final WorkflowStageMapper mapper;
    private final WorkflowTemplateRepository templateRepository;

    public WorkflowStageServiceImpl(
            WorkflowStageRepository repository,
            WorkflowStageMapper mapper,
            WorkflowTemplateRepository templateRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.templateRepository = templateRepository;
    }

    @Override
    public PaginationResponse<WorkflowStageDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<WorkflowStage> workflowStagePage = repository.findAll(pageable);

        PaginationResponse<WorkflowStageDTO> response = new PaginationResponse<>();
        response.setData(workflowStagePage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workflow stages fetched successfully");
        response.setLinks(Links.fromPage(workflowStagePage, "/workflow-stages"));
        response.setMeta(Meta.fromPage(workflowStagePage, "/workflow-stages"));

        return response;
    }

    @Override
    public List<WorkflowStageDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WorkflowStageDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow template not found with id: " + id)));
    }

    @Override
    public WorkflowStageDTO store(WorkflowStageRequest request) {
        WorkflowTemplate workflowTemplate = templateRepository.findById(request.templateId())
                .orElseThrow(() -> new WorkflowTemplateNotFound("Workflow template not found with id:" + request.templateId()));

        WorkflowStage workflowStage = new WorkflowStage();
        workflowStage.setStageName(request.stageName());
        workflowStage.setStageOrder(request.stageOrder());
        workflowStage.setStageType(request.stageType());
        workflowStage.setMandatory(request.isMandatory());
        workflowStage.setCanSkip(request.canSkip());
        workflowStage.setMinApprovers(request.minApprovers());
        workflowStage.setApprovalType(request.approvalType());
        workflowStage.setEscalationHours(request.escalationHours());
        workflowStage.setTemplate(workflowTemplate);

        return mapper.toDTO(repository.save(workflowStage));
    }

    @Override
    public WorkflowStageDTO update(Long id, WorkflowStageRequest request) {
        WorkflowStage workflowStage = repository.findById(id)
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + id));

        WorkflowTemplate workflowTemplate = templateRepository.findById(request.templateId())
                .orElseThrow(() -> new WorkflowTemplateNotFound("Workflow template not found with id:" + request.templateId()));

        updateIfNotNull(request.stageName(), workflowStage::setStageName);
        updateIfNotNull(request.stageOrder(), workflowStage::setStageOrder);
        updateIfNotNull(request.stageType(), workflowStage::setStageType);
        updateIfNotNull(request.isMandatory(), workflowStage::setMandatory);
        updateIfNotNull(request.canSkip(), workflowStage::setCanSkip);
        updateIfNotNull(request.minApprovers(), workflowStage::setMinApprovers);
        updateIfNotNull(request.approvalType(), workflowStage::setApprovalType);
        updateIfNotNull(request.escalationHours(), workflowStage::setEscalationHours);
        workflowStage.setTemplate(workflowTemplate);

        return mapper.toDTO(repository.save(workflowStage));
    }

    @Override
    public void delete(Long id) {
        WorkflowStage workflowStage = repository.findById(id)
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + id));

        repository.delete(workflowStage);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
