package com.betopia.hrm.domain.workflow.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.workflow.enums.ApprovalType;
import com.betopia.hrm.domain.workflow.enums.StageType;
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
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "workflow_stages",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_stage_order", columnNames = {"template_id", "stage_order"})
    }
)
public class WorkflowStage extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stage_name", nullable = false, length = 100)
    private String stageName;

    @Column(name = "stage_order", nullable = false)
    private Integer stageOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type", length = 20, nullable = false)
    private StageType stageType = StageType.APPROVAL;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = true;

    @Column(name = "can_skip", nullable = false)
    private Boolean canSkip = false;

    @Column(name = "min_approvers")
    private Integer minApprovers = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_type", length = 20, nullable = false)
    private ApprovalType approvalType = ApprovalType.SEQUENTIAL;

    @Column(name = "escalation_hours")
    private Integer escalationHours;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private WorkflowTemplate template;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StageApprover> approvers = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<WorkflowInstanceStage> instanceStages = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL)
    private List<ApprovalAction> actions = new ArrayList<>();


    // ===== GETTERS AND SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WorkflowTemplate template) {
        this.template = template;
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

    public List<StageApprover> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<StageApprover> approvers) {
        this.approvers = approvers;
    }

    public List<WorkflowInstanceStage> getInstanceStages() {
        return instanceStages;
    }

    public void setInstanceStages(List<WorkflowInstanceStage> instanceStages) {
        this.instanceStages = instanceStages;
    }

    public List<ApprovalAction> getActions() {
        return actions;
    }

    public void setActions(List<ApprovalAction> actions) {
        this.actions = actions;
    }
}
