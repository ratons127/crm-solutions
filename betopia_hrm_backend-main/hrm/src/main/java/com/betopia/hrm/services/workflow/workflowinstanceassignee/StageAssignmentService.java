package com.betopia.hrm.services.workflow.workflowinstanceassignee;

import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceAssignee;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import com.betopia.hrm.domain.workflow.entity.WorkflowNotification;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import com.betopia.hrm.domain.workflow.enums.AssigneeStatus;
import com.betopia.hrm.domain.workflow.enums.NotificationType;
import com.betopia.hrm.domain.workflow.enums.StageStatus;
import com.betopia.hrm.domain.workflow.repository.StageApproverRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceAssigneeRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceStageRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowNotificationRepository;
import com.betopia.hrm.services.workflow.resolver.ApproverResolverFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StageAssignmentService {

    private final StageApproverRepository stageApproverRepo;
    private final WorkflowInstanceStageRepository instanceStageRepo;
    private final WorkflowInstanceAssigneeRepository assigneeRepo;
    private final WorkflowNotificationRepository notificationRepo;
    private final ApproverResolverFactory resolverFactory;

    public StageAssignmentService(
            StageApproverRepository stageApproverRepo,
            WorkflowInstanceStageRepository instanceStageRepo,
            WorkflowInstanceAssigneeRepository assigneeRepo,
            WorkflowNotificationRepository notificationRepo,
            ApproverResolverFactory resolverFactory
    ) {
        this.stageApproverRepo = stageApproverRepo;
        this.instanceStageRepo = instanceStageRepo;
        this.assigneeRepo = assigneeRepo;
        this.notificationRepo = notificationRepo;
        this.resolverFactory = resolverFactory;
    }

    /**
     * Create a stage instance and assign users (handles user/role/department/reporting_manager/custom).
     * Returns the created WorkflowInstanceStage.
     */
    @Transactional
    public WorkflowInstanceStage assignStage(WorkflowInstance instance, WorkflowStage stage, Long requesterId) {
        // 1) create the stage row (single row per template stage)
        WorkflowInstanceStage instanceStage = new WorkflowInstanceStage();
        instanceStage.setInstance(instance);
        instanceStage.setStage(stage);
        instanceStage.setStageStatus(StageStatus.PENDING);
        instanceStage.setAssignedAt(Instant.now());
        instanceStage = instanceStageRepo.save(instanceStage);

        // 2) resolve approvers from stage_approvers rows
        StageApprover approverDef = stageApproverRepo.findByStageIdAndStatusTrueOrderBySequenceOrderAsc(stage.getId());

        Set<Long> resolvedUserIds = resolveApprovers(requesterId, approverDef);

        /*for (StageApprover def : approverDefs) {
            switch (def.getApproverType()) {
                case USER:
                    if (def.getUserId() != null) resolvedUserIds.add(def.getUserId());
                    break;
                case ROLE:
                    // find users with role
                    List<Long> usersByRole = userRepo.findUserIdsByRoleId(def.getRoleId());
                    resolvedUserIds.addAll(usersByRole);
                    break;
                case DEPARTMENT:
                    List<Long> usersByDept = userRepo.findUserIdsByDepartmentId(def.getDepartmentId());
                    resolvedUserIds.addAll(usersByDept);
                    break;
                case REPORTING_MANAGER:
                    Long managerId = employeeService.findReportingManagerId(requesterId);
                    if (managerId != null) resolvedUserIds.add(managerId);
                    break;
                case CUSTOM:
                    // call custom resolver (optional plugin)
                    List<Long> usersCustom = resolveCustomApprover(def, instance.getReferenceId());
                    resolvedUserIds.addAll(usersCustom);
                    break;
            }
        }*/

        // 3) persist assignees
        List<WorkflowInstanceAssignee> assignees = new ArrayList<>();
        for (Long userId : resolvedUserIds) {
            WorkflowInstanceAssignee assignee = new WorkflowInstanceAssignee();
            assignee.setInstanceStage(instanceStage);
            assignee.setUserId(userId);
            assignee.setStatus(AssigneeStatus.PENDING);
            assignees.add(assignee);
        }
        assigneeRepo.saveAll(assignees);

        // 4) send notifications to each assignee
        for (Long userId : resolvedUserIds) {
            WorkflowNotification note = new WorkflowNotification();
            note.setInstance(instance);
            note.setNotificationType(NotificationType.ASSIGNMENT);
            note.setRecipientId(userId);
            note.setSubject("Task assigned: " + instance.getReferenceNumber());
            note.setMessage("You have a task to approve: " + instance.getReferenceNumber());
            notificationRepo.save(note);
        }

        return instanceStage;
    }

    public Set<Long> resolveApprovers(Long requesterId, StageApprover approverDef) {
        return resolverFactory.resolve(approverDef.getApproverType(), requesterId, approverDef);
    }
}
