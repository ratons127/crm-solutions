package com.betopia.hrm.domain.workflow.entity;

import com.betopia.hrm.domain.workflow.enums.AssigneeStatus;
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
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "workflow_instance_assignees")
public class WorkflowInstanceAssignee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_stage_id")
    private WorkflowInstanceStage instanceStage;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssigneeStatus status = AssigneeStatus.PENDING;

    @Column(name = "acted_at")
    private Instant actedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowInstanceStage getInstanceStage() {
        return instanceStage;
    }

    public void setInstanceStage(WorkflowInstanceStage instanceStage) {
        this.instanceStage = instanceStage;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AssigneeStatus getStatus() {
        return status;
    }

    public void setStatus(AssigneeStatus status) {
        this.status = status;
    }

    public Instant getActedAt() {
        return actedAt;
    }

    public void setActedAt(Instant actedAt) {
        this.actedAt = actedAt;
    }
}
