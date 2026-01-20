package com.betopia.hrm.domain.workflow.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.workflow.enums.WorkflowPriority;
import com.betopia.hrm.domain.workflow.enums.WorkflowStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflow_instances")
public class WorkflowInstance extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId; // FK to Leave Request, etc.

    @Column(name = "reference_number", length = 50)
    private String referenceNumber;

    @Column(name = "initiated_by", nullable = false)
    private Long initiatedBy;

    @Column(name = "initiated_at", nullable = false, updatable = false)
    private Instant initiatedAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", length = 20, nullable = false)
    private WorkflowStatus currentStatus = WorkflowStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 10, nullable = false)
    private WorkflowPriority priority = WorkflowPriority.NORMAL;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private WorkflowTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_stage_id")
    private WorkflowStage currentStage;

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowInstanceStage> stages = new ArrayList<>();

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalAction> actions = new ArrayList<>();

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowNotification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowEscalation> escalations = new ArrayList<>();

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowAuditLog> auditLogs = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public WorkflowTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WorkflowTemplate template) {
        this.template = template;
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

    public WorkflowStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(WorkflowStage currentStage) {
        this.currentStage = currentStage;
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

    public List<WorkflowInstanceStage> getStages() {
        return stages;
    }

    public void setStages(List<WorkflowInstanceStage> stages) {
        this.stages = stages;
    }

    public List<ApprovalAction> getActions() {
        return actions;
    }

    public void setActions(List<ApprovalAction> actions) {
        this.actions = actions;
    }

    public List<WorkflowNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<WorkflowNotification> notifications) {
        this.notifications = notifications;
    }

    public List<WorkflowEscalation> getEscalations() {
        return escalations;
    }

    public void setEscalations(List<WorkflowEscalation> escalations) {
        this.escalations = escalations;
    }

    public List<WorkflowAuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<WorkflowAuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
}
