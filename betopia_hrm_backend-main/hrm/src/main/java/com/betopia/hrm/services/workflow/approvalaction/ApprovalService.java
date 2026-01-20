package com.betopia.hrm.services.workflow.approvalaction;

import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import com.betopia.hrm.domain.workflow.entity.WorkflowAuditLog;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceAssignee;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowNotification;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.enums.ActionType;
import com.betopia.hrm.domain.workflow.enums.ApprovalType;
import com.betopia.hrm.domain.workflow.enums.AssigneeStatus;
import com.betopia.hrm.domain.workflow.enums.NotificationType;
import com.betopia.hrm.domain.workflow.enums.StageStatus;
import com.betopia.hrm.domain.workflow.enums.WorkflowStatus;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceAssigneeNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceStageNotFound;
import com.betopia.hrm.domain.workflow.repository.ApprovalActionRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowAuditLogRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceAssigneeRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceStageRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowNotificationRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowStageRepository;
import com.betopia.hrm.domain.workflow.request.ApproveRequest;
import com.betopia.hrm.services.workflow.workflowinstanceassignee.StageAssignmentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ApprovalService {

    private final WorkflowInstanceRepository instanceRepo;
    private final WorkflowInstanceStageRepository instanceStageRepo;
    private final WorkflowInstanceAssigneeRepository assigneeRepo;
    private final ApprovalActionRepository actionRepo;
    private final WorkflowStageRepository stageRepo;
    private final WorkflowInstanceAssigneeRepository instanceAssigneeRepo;
    private final WorkflowNotificationRepository notificationRepo;
    private final WorkflowAuditLogRepository auditRepo;
    private final WorkflowInstanceRepository workflowInstanceRepo;
    private final StageAssignmentService assignmentService;

    public ApprovalService(WorkflowInstanceRepository instanceRepo, WorkflowInstanceStageRepository instanceStageRepo, WorkflowInstanceAssigneeRepository assigneeRepo, ApprovalActionRepository actionRepo, WorkflowStageRepository stageRepo, WorkflowInstanceAssigneeRepository instanceAssigneeRepo, WorkflowNotificationRepository notificationRepo, WorkflowAuditLogRepository auditRepo, WorkflowInstanceRepository workflowInstanceRepo, StageAssignmentService assignmentService) {
        this.instanceRepo = instanceRepo;
        this.instanceStageRepo = instanceStageRepo;
        this.assigneeRepo = assigneeRepo;
        this.actionRepo = actionRepo;
        this.stageRepo = stageRepo;
        this.instanceAssigneeRepo = instanceAssigneeRepo;
        this.notificationRepo = notificationRepo;
        this.auditRepo = auditRepo;
        this.workflowInstanceRepo = workflowInstanceRepo;
        this.assignmentService = assignmentService;
    }

    @Transactional
    public void approve(ApproveRequest request) {
        WorkflowInstance instance = instanceRepo.findById(request.instanceId())
            .orElseThrow(() -> new WorkflowInstanceNotFound("instance"));

        WorkflowInstanceStage instStage = instanceStageRepo.findById(request.instanceStageId())
            .orElseThrow(() -> new WorkflowInstanceStageNotFound("instance stage"));

        // validate approver is assigned and pending
        WorkflowInstanceAssignee assignee = assigneeRepo.findByInstanceStageIdAndUserId(instStage.getId(), request.approverId());

        if (assignee.getStatus() != AssigneeStatus.PENDING) {
            throw new IllegalStateException("already acted");
        }

        // 1) insert approval_action
        ApprovalAction action = new ApprovalAction();
        action.setInstance(instance);
        action.setInstanceStage(instStage);
        action.setStage(instStage.getStage());
        action.setActionBy(request.approverId());
        action.setActionType(ActionType.APPROVED);
        action.setComments(request.comments());
        actionRepo.save(action);

        // 2) update assignee record
        assignee.setStatus(AssigneeStatus.APPROVED);
        assignee.setActedAt(Instant.now());
        assigneeRepo.save(assignee);

        // 3) evaluate if stage is complete (depends on stage.approvalType and minApprovers)
        boolean stageCompleted = evaluateStageCompletion(instStage);

        if (stageCompleted) {
            instStage.setStageStatus(StageStatus.APPROVED);
            instStage.setCompletedAt(Instant.now());
            instanceStageRepo.save(instStage);

            // move next
            moveToNextStage(instance, instStage.getStage());
        } else {
            // leave stage pending (waiting for other approvers)
            instStage.setStageStatus(StageStatus.IN_PROGRESS);
            instanceStageRepo.save(instStage);
        }

        // audit
        WorkflowAuditLog audit = new WorkflowAuditLog();
        audit.setInstance(instance);
        audit.setActionType("APPROVED");
        audit.setPerformedBy(request.approverId());
        audit.setFieldChanged("instance_stage_id");
        audit.setOldValue("assignee_status=PENDING");
        audit.setNewValue("assignee_status=APPROVED");
        auditRepo.save(audit);
    }

    private boolean evaluateStageCompletion(WorkflowInstanceStage instStage) {
        WorkflowStage stageDef = instStage.getStage();
        StageStatus current = instStage.getStageStatus();
        List<WorkflowInstanceAssignee> pending = assigneeRepo.findByInstanceStageIdAndStatus(instStage.getId(), AssigneeStatus.PENDING);

        // Approval types:
        // SEQUENTIAL: first approver's approval suffices (if sequence defined as single?)
        // PARALLEL: need minApprovers approvals across assignees
        // ANY_ONE: one approval completes

        ApprovalType approvalType = stageDef.getApprovalType();
        int minApprovers = (stageDef.getMinApprovers() == null) ? 1 : stageDef.getMinApprovers();

        List<WorkflowInstanceAssignee> allAssignees = assigneeRepo.findByInstanceStageIdAndStatus(instStage.getId(), AssigneeStatus.PENDING);
        // Actually we need counts of APPROVED -- get all assignees
        List<WorkflowInstanceAssignee> all = assigneeRepo.findByInstanceStageId(instStage.getId());

        long approvedCount = all.stream()
                .filter(a -> a.getStatus() == AssigneeStatus.APPROVED)
                .count();
        long total = all.size();

        return switch (approvalType) {
            case ANY_ONE -> approvedCount >= 1;
            case PARALLEL -> approvedCount >= minApprovers;
            case SEQUENTIAL ->
                // sequential: we expect assignees ordered by sequence. Here we treat as ANY_ONE for the first in order.
                // If you want strict sequential enforcement, ensure assign order and only first pending can act.
                // Implement: allow only the lowest-sequence pending to act; if they approved, stageCompleted true if minApprovers satisfied.
                    approvedCount >= minApprovers;
        };
    }

    @Transactional
    public void moveToNextStage(WorkflowInstance instance, WorkflowStage currentStage) {
        // 1) find next stage by order
        int nextOrder = currentStage.getStageOrder() + 1;
        Optional<WorkflowStage> nextOpt = stageRepo.findByTemplateIdAndStageOrder(currentStage.getTemplate().getId(), nextOrder);

        if (nextOpt.isPresent()) {
            WorkflowStage next = nextOpt.get();

            // create next instance stage and assign (resolve approvers)
            WorkflowInstanceStage nextInstanceStage = assignmentService.assignStage(instance, next, instance.getInitiatedBy());

            // update instance current_stage
            instance.setCurrentStage(next);
            instance.setCurrentStatus(WorkflowStatus.IN_PROGRESS);
            instanceRepo.save(instance);

            // notify via notification table created inside assignStage

            // audit
            WorkflowAuditLog audit = new WorkflowAuditLog();
            audit.setInstance(instance);
            audit.setActionType("STAGE_MOVED");
            audit.setPerformedBy(null);
            audit.setOldValue("stage=" + currentStage.getStageName());
            audit.setNewValue("stage=" + next.getStageName());
            auditRepo.save(audit);

        } else {
            // no more stages → mark instance approved/completed
            instance.setCurrentStage(null);
            instance.setCurrentStatus(WorkflowStatus.APPROVED);
            instance.setCompletedAt(Instant.now());
            instanceRepo.save(instance);

            // final notification
            WorkflowNotification completion = new WorkflowNotification();
            completion.setInstance(instance);
            completion.setNotificationType(NotificationType.COMPLETION);
            completion.setRecipientId(instance.getInitiatedBy());
            completion.setSubject("Workflow completed: " + instance.getReferenceNumber());
            completion.setMessage("Your request has been approved.");
            notificationRepo.save(completion);

            // audit
            WorkflowAuditLog a = new WorkflowAuditLog();
            a.setInstance(instance);
            a.setActionType("COMPLETED");
            a.setNewValue("status=APPROVED");
            auditRepo.save(a);
        }
    }
}
