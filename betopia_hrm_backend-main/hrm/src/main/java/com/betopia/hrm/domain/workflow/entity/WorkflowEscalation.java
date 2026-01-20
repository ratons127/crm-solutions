package com.betopia.hrm.domain.workflow.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "workflow_escalations")
public class WorkflowEscalation extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "escalated_from", nullable = false)
    private Long escalatedFrom; // previous approver ID

    @Column(name = "escalated_to", nullable = false)
    private Long escalatedTo; // new approver ID after escalation

    @Column(name = "escalation_reason", length = 255)
    private String escalationReason;

    @Column(name = "escalated_at", nullable = false)
    private Instant escalatedAt = Instant.now();

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instance_id", nullable = false)
    private WorkflowInstance instance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instance_stage_id", nullable = false)
    private WorkflowInstanceStage instanceStage;

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowInstance getInstance() {
        return instance;
    }

    public void setInstance(WorkflowInstance instance) {
        this.instance = instance;
    }

    public WorkflowInstanceStage getInstanceStage() {
        return instanceStage;
    }

    public void setInstanceStage(WorkflowInstanceStage instanceStage) {
        this.instanceStage = instanceStage;
    }

    public Long getEscalatedFrom() {
        return escalatedFrom;
    }

    public void setEscalatedFrom(Long escalatedFrom) {
        this.escalatedFrom = escalatedFrom;
    }

    public Long getEscalatedTo() {
        return escalatedTo;
    }

    public void setEscalatedTo(Long escalatedTo) {
        this.escalatedTo = escalatedTo;
    }

    public String getEscalationReason() {
        return escalationReason;
    }

    public void setEscalationReason(String escalationReason) {
        this.escalationReason = escalationReason;
    }

    public Instant getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(Instant escalatedAt) {
        this.escalatedAt = escalatedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
}
