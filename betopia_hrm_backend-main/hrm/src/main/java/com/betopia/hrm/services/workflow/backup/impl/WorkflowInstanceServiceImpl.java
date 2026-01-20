package com.betopia.hrm.services.workflow.backup.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.WorkflowInstanceMapper;
import com.betopia.hrm.domain.workflow.entity.Module;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowTemplate;
import com.betopia.hrm.domain.workflow.exceptions.ModuleNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowStageNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowTemplateNotFound;
import com.betopia.hrm.domain.workflow.repository.ModuleRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowStageRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowTemplateRepository;
import com.betopia.hrm.domain.workflow.request.WorkflowInstanceRequest;
import com.betopia.hrm.services.workflow.backup.WorkflowInstanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

@Service
public class WorkflowInstanceServiceImpl implements WorkflowInstanceService {

    private final WorkflowInstanceRepository repository;
    private final WorkflowInstanceMapper mapper;
    private final WorkflowStageRepository stageRepository;
    private final WorkflowTemplateRepository templateRepository;
    private final ModuleRepository moduleRepository;

    public WorkflowInstanceServiceImpl(
            WorkflowInstanceRepository repository,
            WorkflowInstanceMapper mapper,
            WorkflowStageRepository stageRepository,
            WorkflowTemplateRepository templateRepository,
            ModuleRepository moduleRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.stageRepository = stageRepository;
        this.templateRepository = templateRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public PaginationResponse<WorkflowInstanceDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<WorkflowInstance> workflowInstancePage = repository.findAll(pageable);

        PaginationResponse<WorkflowInstanceDTO> response = new PaginationResponse<>();
        response.setData(workflowInstancePage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workflow instance fetched successfully");
        response.setLinks(Links.fromPage(workflowInstancePage, "/workflow-instances"));
        response.setMeta(Meta.fromPage(workflowInstancePage, "/workflow-instances"));

        return response;
    }

    @Override
    public List<WorkflowInstanceDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WorkflowInstanceDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id: " + id)));
    }

    @Override
    public WorkflowInstanceDTO store(WorkflowInstanceRequest request) {
        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ModuleNotFound("Module not found with id:" + request.moduleId()));

        WorkflowTemplate workflowTemplate = templateRepository.findById(request.templateId())
                .orElseThrow(() -> new WorkflowTemplateNotFound("Workflow template not found with id:" + request.templateId()));

        WorkflowStage workflowStage = stageRepository.findById(request.currentStageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + request.currentStageId()));

        WorkflowInstance workflowInstance = new WorkflowInstance();
        workflowInstance.setReferenceId(request.referenceId());
        workflowInstance.setReferenceNumber(request.referenceNumber());
        workflowInstance.setInitiatedBy(request.initiatedBy());
        workflowInstance.setInitiatedAt(Instant.now());
        workflowInstance.setCurrentStatus(request.currentStatus());
        workflowInstance.setPriority(request.priority());
        workflowInstance.setCompletedAt(request.completedAt());
        workflowInstance.setRemarks(request.remarks());
        workflowInstance.setModule(module);
        workflowInstance.setTemplate(workflowTemplate);
        workflowInstance.setCurrentStage(workflowStage);

        return mapper.toDTO(repository.save(workflowInstance));
    }

    @Override
    public WorkflowInstanceDTO update(Long id, WorkflowInstanceRequest request) {
        WorkflowInstance workflowInstance = repository.findById(id)
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + id));

        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ModuleNotFound("Module not found with id:" + request.moduleId()));

        WorkflowTemplate workflowTemplate = templateRepository.findById(request.templateId())
                .orElseThrow(() -> new WorkflowTemplateNotFound("Workflow template not found with id:" + request.templateId()));

        WorkflowStage workflowStage = stageRepository.findById(request.currentStageId())
                .orElseThrow(() -> new WorkflowStageNotFound("Workflow stage not found with id:" + request.currentStageId()));

        updateIfNotNull(request.referenceId(), workflowInstance::setReferenceId);
        updateIfNotNull(request.referenceNumber(), workflowInstance::setReferenceNumber);
        updateIfNotNull(request.currentStatus(), workflowInstance::setCurrentStatus);
        updateIfNotNull(request.priority(), workflowInstance::setPriority);
        updateIfNotNull(request.completedAt(), workflowInstance::setCompletedAt);
        updateIfNotNull(request.remarks(), workflowInstance::setRemarks);
        workflowInstance.setModule(module);
        workflowInstance.setTemplate(workflowTemplate);
        workflowInstance.setCurrentStage(workflowStage);

        return mapper.toDTO(repository.save(workflowInstance));
    }

    @Override
    public void delete(Long id) {
        WorkflowInstance workflowInstance = repository.findById(id)
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + id));

        repository.delete(workflowInstance);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
