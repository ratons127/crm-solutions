package com.betopia.hrm.services.workflow.workflowinstance;

import com.betopia.hrm.domain.workflow.entity.Module;
import com.betopia.hrm.domain.workflow.entity.WorkflowAuditLog;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowTemplate;
import com.betopia.hrm.domain.workflow.enums.WorkflowStatus;
import com.betopia.hrm.domain.workflow.repository.ModuleRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowAuditLogRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowStageRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowTemplateRepository;
import com.betopia.hrm.domain.workflow.request.StartWorkflowRequest;
import com.betopia.hrm.services.workflow.workflowinstanceassignee.StageAssignmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WorkflowInstanceService {

    private final WorkflowInstanceRepository instanceRepo;
    private final WorkflowTemplateRepository templateRepo;
    private final WorkflowStageRepository stageRepo;
    private final StageAssignmentService assignmentService;
    private final WorkflowAuditLogRepository auditRepo;
    private final ModuleRepository moduleRepo;

    public WorkflowInstanceService(
            WorkflowInstanceRepository instanceRepo,
            WorkflowTemplateRepository templateRepo,
            WorkflowStageRepository stageRepo,
            StageAssignmentService assignmentService,
            WorkflowAuditLogRepository auditRepo,
            ModuleRepository moduleRepo
    ) {
        this.instanceRepo = instanceRepo;
        this.templateRepo = templateRepo;
        this.stageRepo = stageRepo;
        this.assignmentService = assignmentService;
        this.auditRepo = auditRepo;
        this.moduleRepo = moduleRepo;
    }

    @Transactional
    public WorkflowInstance startWorkflow(StartWorkflowRequest request) {
        // 1) create instance
        Module module = moduleRepo.findById(request.moduleId())
                .orElseThrow(() -> new IllegalArgumentException("Module not found"));
        WorkflowTemplate template = templateRepo.findById(request.templateId())
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        WorkflowInstance instance = new WorkflowInstance();
        instance.setModule(module);
        instance.setTemplate(template);
        instance.setReferenceId(request.referenceId());
        instance.setReferenceNumber(request.referenceNumber());
        instance.setInitiatedBy(request.initiatedBy());
        instance.setInitiatedAt(Instant.now());
        instance.setCurrentStatus(WorkflowStatus.PENDING);
        instance = instanceRepo.save(instance);

        // 2) get first stage
        List<WorkflowStage> stages = stageRepo.findByTemplateIdOrderByStageOrderAsc(template.getId());
        if (stages.isEmpty()) throw new IllegalStateException("template has no stages");
        WorkflowStage first = stages.get(0);

        // 3) create and assign stage & assignees
        WorkflowInstanceStage instanceStage = assignmentService.assignStage(instance, first, request.initiatedBy());

        // 4) update instance current stage
        instance.setCurrentStage(first);
        instance.setCurrentStatus(WorkflowStatus.IN_PROGRESS);
        instanceRepo.save(instance);

        // 5) audit
        WorkflowAuditLog audit = new WorkflowAuditLog();
        audit.setInstance(instance);
        audit.setActionType("INITIATED");
        audit.setPerformedBy(request.initiatedBy());
        audit.setNewValue("status=PENDING;stage=" + first.getStageName());
        auditRepo.save(audit);

        return instance;
    }
}
