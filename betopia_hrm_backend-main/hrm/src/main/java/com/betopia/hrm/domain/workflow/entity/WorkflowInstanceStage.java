package com.betopia.hrm.domain.workflow.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.workflow.enums.StageStatus;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflow_instance_stages")
public class WorkflowInstanceStage extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage_status", nullable = false, length = 20)
    private StageStatus stageStatus = StageStatus.PENDING;

    @Column(name = "assigned_to")
    private Long assignedTo;

    @Column(name = "assigned_at")
    private Instant assignedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instance_id", nullable = false)
    private WorkflowInstance instance;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private WorkflowStage stage;

    @OneToMany(mappedBy = "instanceStage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApprovalAction> actions = new ArrayList<>();

    // ===== GETTERS AND SETTERS =====

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

    public WorkflowStage getStage() {
        return stage;
    }

    public void setStage(WorkflowStage stage) {
        this.stage = stage;
    }

    public StageStatus getStageStatus() {
        return stageStatus;
    }

    public void setStageStatus(StageStatus stageStatus) {
        this.stageStatus = stageStatus;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Instant assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
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

    public List<ApprovalAction> getActions() {
        return actions;
    }

    public void setActions(List<ApprovalAction> actions) {
        this.actions = actions;
    }
}
