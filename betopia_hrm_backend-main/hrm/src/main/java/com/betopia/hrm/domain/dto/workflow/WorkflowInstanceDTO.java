package com.betopia.hrm.domain.dto.workflow;

import com.betopia.hrm.domain.workflow.enums.WorkflowPriority;
import com.betopia.hrm.domain.workflow.enums.WorkflowStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WorkflowInstanceDTO {
    private Long id;
    private Long referenceId; // FK to Leave Request, etc.
    private String referenceNumber;
    private Long initiatedBy;
    private Instant initiatedAt;
    private WorkflowStatus currentStatus;
    private WorkflowPriority priority;
    private Instant completedAt;
    private String remarks;
    private Long moduleId;
    private Long templateId;
    private Long currentStageId;
    private List<WorkflowInstanceStageDTO> stagesDtos = new ArrayList<>();
    private List<ApprovalActionDTO> actionsDtos = new ArrayList<>();
    private List<WorkflowNotificationDTO> notificationsDtos = new ArrayList<>();
    private List<WorkflowEscalationDTO> escalationsDtos = new ArrayList<>();
    private List<WorkflowAuditLogDTO> auditLogsDtos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Long getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(Long initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public Instant getInitiatedAt() {
        return initiatedAt;
    }

    public void setInitiatedAt(Instant initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public WorkflowStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(WorkflowStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    public WorkflowPriority getPriority() {
        return priority;
    }

    public void setPriority(WorkflowPriority priority) {
        this.priority = priority;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getCurrentStageId() {
        return currentStageId;
    }

    public void setCurrentStageId(Long currentStageId) {
        this.currentStageId = currentStageId;
    }

    public List<WorkflowInstanceStageDTO> getStagesDtos() {
        return stagesDtos;
    }

    public void setStagesDtos(List<WorkflowInstanceStageDTO> stagesDtos) {
        this.stagesDtos = stagesDtos;
    }

    public List<ApprovalActionDTO> getActionsDtos() {
        return actionsDtos;
    }

    public void setActionsDtos(List<ApprovalActionDTO> actionsDtos) {
        this.actionsDtos = actionsDtos;
    }

    public List<WorkflowNotificationDTO> getNotificationsDtos() {
        return notificationsDtos;
    }

    public void setNotificationsDtos(List<WorkflowNotificationDTO> notificationsDtos) {
        this.notificationsDtos = notificationsDtos;
    }

    public List<WorkflowEscalationDTO> getEscalationsDtos() {
        return escalationsDtos;
    }

    public void setEscalationsDtos(List<WorkflowEscalationDTO> escalationsDtos) {
        this.escalationsDtos = escalationsDtos;
    }

    public List<WorkflowAuditLogDTO> getAuditLogsDtos() {
        return auditLogsDtos;
    }

    public void setAuditLogsDtos(List<WorkflowAuditLogDTO> auditLogsDtos) {
        this.auditLogsDtos = auditLogsDtos;
    }
}
