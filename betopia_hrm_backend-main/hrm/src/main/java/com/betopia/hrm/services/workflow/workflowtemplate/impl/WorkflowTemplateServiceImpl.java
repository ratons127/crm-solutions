package com.betopia.hrm.services.workflow.workflowtemplate.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowTemplateDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.WorkflowTemplateMapper;
import com.betopia.hrm.domain.workflow.entity.Module;
import com.betopia.hrm.domain.workflow.entity.WorkflowTemplate;
import com.betopia.hrm.domain.workflow.exceptions.ModuleNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowTemplateNotFound;
import com.betopia.hrm.domain.workflow.repository.ModuleRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowTemplateRepository;
import com.betopia.hrm.domain.workflow.request.WorkflowTemplateRequest;
import com.betopia.hrm.services.workflow.workflowtemplate.WorkflowTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class WorkflowTemplateServiceImpl implements WorkflowTemplateService {

    private final WorkflowTemplateRepository repository;
    private final WorkflowTemplateMapper mapper;
    private final ModuleRepository moduleRepository;

    public WorkflowTemplateServiceImpl(
            WorkflowTemplateRepository repository,
            WorkflowTemplateMapper mapper,
            ModuleRepository moduleRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public PaginationResponse<WorkflowTemplateDTO> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<WorkflowTemplate> workflowTemplatePage = repository.findAll(pageable);

        PaginationResponse<WorkflowTemplateDTO> response = new PaginationResponse<>();
        response.setData(workflowTemplatePage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workflow template fetched successfully");
        response.setLinks(Links.fromPage(workflowTemplatePage, "/workflow-templates"));
        response.setMeta(Meta.fromPage(workflowTemplatePage, "/workflow-templates"));

        return response;
    }

    @Override
    public List<WorkflowTemplateDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WorkflowTemplateDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new WorkflowTemplateNotFound("Workflow Template not found with id: " + id)));
    }

    @Override
    public WorkflowTemplateDTO store(WorkflowTemplateRequest request) {
        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ModuleNotFound("Module not found with id:" + request.moduleId()));

        WorkflowTemplate workflowTemplate=new WorkflowTemplate();
        workflowTemplate.setTemplateName(request.templateName());
        workflowTemplate.setDescription(request.description());
        workflowTemplate.setModule(module);

        return mapper.toDTO(repository.save(workflowTemplate));
    }

    @Override
    public WorkflowTemplateDTO update(Long id, WorkflowTemplateRequest request) {
        WorkflowTemplate workflowTemplate = repository.findById(id)
                .orElseThrow(() -> new WorkflowTemplateNotFound("Workflow template not found with id:" + id));

        Module module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ModuleNotFound("Module not found with id:" + request.moduleId()));

        workflowTemplate.setModule(module);
        updateIfNotNull(request.templateName(), workflowTemplate::setTemplateName);
        updateIfNotNull(request.description(), workflowTemplate::setDescription);
        updateIfNotNull(request.isDefault(), workflowTemplate::setDefault);
        updateIfNotNull(request.status(), workflowTemplate::setStatus);

        return mapper.toDTO(repository.save(workflowTemplate));
    }

    @Override
    public void delete(Long id) {
        WorkflowTemplate workflowTemplate = repository.findById(id)
                .orElseThrow(() -> new WorkflowTemplateNotFound("Workflow template not found with id:" + id));

        repository.delete(workflowTemplate);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
