package com.betopia.hrm.domain.dto.workflow;

import java.time.Instant;

public class WorkflowEscalationDTO {

    private Long id;
    private Long escalatedFrom; // previous approver ID
    private Long escalatedTo; // new approver ID after escalation
    private String escalationReason;
    private Instant escalatedAt;
    private Instant resolvedAt;
    private Long instanceId;
    private Long instanceStageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getInstanceStageId() {
        return instanceStageId;
    }

    public void setInstanceStageId(Long instanceStageId) {
        this.instanceStageId = instanceStageId;
    }
}
