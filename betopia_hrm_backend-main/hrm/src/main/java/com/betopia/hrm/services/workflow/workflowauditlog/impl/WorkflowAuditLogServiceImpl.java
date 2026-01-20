package com.betopia.hrm.services.workflow.workflowauditlog.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.WorkflowAuditLogDTO;
import com.betopia.hrm.domain.dto.workflow.WorkflowEscalationDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.WorkflowAuditLogMapper;
import com.betopia.hrm.domain.workflow.entity.WorkflowAuditLog;
import com.betopia.hrm.domain.workflow.entity.WorkflowEscalation;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowAuditLogNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.repository.WorkflowAuditLogRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.request.WorkflowAuditLogRequest;
import com.betopia.hrm.services.workflow.workflowauditlog.WorkflowAuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class WorkflowAuditLogServiceImpl implements WorkflowAuditLogService {

    private final WorkflowAuditLogRepository repository;
    private final WorkflowAuditLogMapper mapper;
    private final WorkflowInstanceRepository instanceRepository;

    public WorkflowAuditLogServiceImpl(
            WorkflowAuditLogRepository repository,
            WorkflowAuditLogMapper mapper,
            WorkflowInstanceRepository instanceRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.instanceRepository = instanceRepository;
    }

    @Override
    public PaginationResponse<WorkflowAuditLogDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<WorkflowAuditLog> workflowEscalationPage = repository.findAll(pageable);

        PaginationResponse<WorkflowAuditLogDTO> response = new PaginationResponse<>();
        response.setData(workflowEscalationPage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workflow audit logs fetched successfully");
        response.setLinks(Links.fromPage(workflowEscalationPage, "/workflow-audit-logs"));
        response.setMeta(Meta.fromPage(workflowEscalationPage, "/workflow-audit-logs"));

        return response;
    }

    @Override
    public List<WorkflowAuditLogDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WorkflowAuditLogDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new WorkflowAuditLogNotFound("Workflow audit log not found with id: " + id)));
    }

    @Override
    public WorkflowAuditLogDTO store(WorkflowAuditLogRequest request) {
        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        WorkflowAuditLog auditLog=new WorkflowAuditLog();
        auditLog.setActionType(request.actionType());
        auditLog.setPerformedBy(request.performedBy());
        auditLog.setOldValue(request.oldValue());
        auditLog.setNewValue(request.newValue());
        auditLog.setFieldChanged(request.fieldChanged());
        auditLog.setIpAddress(request.ipAddress());
        auditLog.setInstance(instance);

        return mapper.toDTO(repository.save(auditLog));
    }

    @Override
    public WorkflowAuditLogDTO update(Long id, WorkflowAuditLogRequest request) {
        WorkflowAuditLog auditLog = repository.findById(id)
                .orElseThrow(() -> new WorkflowAuditLogNotFound("Workflow audit log not found with id:" + id));

        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        updateIfNotNull(request.actionType(), auditLog::setActionType);
        updateIfNotNull(request.performedBy(), auditLog::setPerformedBy);
        updateIfNotNull(request.oldValue(), auditLog::setOldValue);
        updateIfNotNull(request.newValue(), auditLog::setNewValue);
        updateIfNotNull(request.fieldChanged(), auditLog::setFieldChanged);
        updateIfNotNull(request.ipAddress(), auditLog::setIpAddress);
        auditLog.setInstance(instance);

        return mapper.toDTO(repository.save(auditLog));
    }

    @Override
    public void delete(Long id) {
        WorkflowAuditLog auditLog = repository.findById(id)
                .orElseThrow(() -> new WorkflowAuditLogNotFound("Workflow audit log not found with id:" + id));

        repository.delete(auditLog);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
