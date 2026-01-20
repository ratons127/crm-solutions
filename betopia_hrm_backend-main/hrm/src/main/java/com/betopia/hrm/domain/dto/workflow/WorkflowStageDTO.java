package com.betopia.hrm.domain.dto.workflow;

import com.betopia.hrm.domain.workflow.enums.ApprovalType;
import com.betopia.hrm.domain.workflow.enums.StageType;

import java.util.ArrayList;
import java.util.List;

public class WorkflowStageDTO {

    private Long id;
    private String stageName;
    private Integer stageOrder;
    private StageType stageType;
    private Boolean isMandatory;
    private Boolean canSkip;
    private Integer minApprovers;
    private ApprovalType approvalType;
    private Integer escalationHours;
    private Long templateId;
    private List<StageApproverDTO> approversDtos = new ArrayList<>();
    private List<WorkflowInstanceStageDTO> instanceStagesDtos = new ArrayList<>();
    private List<ApprovalActionDTO> actionsDtos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getStageOrder() {
        return stageOrder;
    }

    public void setStageOrder(Integer stageOrder) {
        this.stageOrder = stageOrder;
    }

    public StageType getStageType() {
        return stageType;
    }

    public void setStageType(StageType stageType) {
        this.stageType = stageType;
    }

    public Boolean getMandatory() {
        return isMandatory;
    }

    public void setMandatory(Boolean mandatory) {
        isMandatory = mandatory;
    }

    public Boolean getCanSkip() {
        return canSkip;
    }

    public void setCanSkip(Boolean canSkip) {
        this.canSkip = canSkip;
    }

    public Integer getMinApprovers() {
        return minApprovers;
    }

    public void setMinApprovers(Integer minApprovers) {
        this.minApprovers = minApprovers;
    }

    public ApprovalType getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(ApprovalType approvalType) {
        this.approvalType = approvalType;
    }

    public Integer getEscalationHours() {
        return escalationHours;
    }

    public void setEscalationHours(Integer escalationHours) {
        this.escalationHours = escalationHours;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public List<StageApproverDTO> getApproversDtos() {
        return approversDtos;
    }

    public void setApproversDtos(List<StageApproverDTO> approversDtos) {
        this.approversDtos = approversDtos;
    }

    public List<WorkflowInstanceStageDTO> getInstanceStagesDtos() {
        return instanceStagesDtos;
    }

    public void setInstanceStagesDtos(List<WorkflowInstanceStageDTO> instanceStagesDtos) {
        this.instanceStagesDtos = instanceStagesDtos;
    }

    public List<ApprovalActionDTO> getActionsDtos() {
        return actionsDtos;
    }

    public void setActionsDtos(List<ApprovalActionDTO> actionsDtos) {
        this.actionsDtos = actionsDtos;
    }
}
