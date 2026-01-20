package com.betopia.hrm.domain.dto.workflow;

import com.betopia.hrm.domain.workflow.enums.StageStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class WorkflowInstanceStageDTO {

    private Long id;
    private StageStatus stageStatus;
    private Long assignedTo;
    private Instant assignedAt;
    private Instant startedAt;
    private Instant completedAt;
    private String remarks;
    private Long instanceId;
    private Long stageId;
    private List<ApprovalActionDTO> actionsDtos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public List<ApprovalActionDTO> getActionsDtos() {
        return actionsDtos;
    }

    public void setActionsDtos(List<ApprovalActionDTO> actionsDtos) {
        this.actionsDtos = actionsDtos;
    }
}
