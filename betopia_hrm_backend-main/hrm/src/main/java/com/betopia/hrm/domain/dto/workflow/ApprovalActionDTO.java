package com.betopia.hrm.domain.dto.workflow;

import com.betopia.hrm.domain.workflow.enums.ActionType;

import java.time.Instant;

public class ApprovalActionDTO {

    private Long id;
    private Long actionBy;
    private ActionType actionType;
    private Instant actionDate = Instant.now();
    private String comments;
    private String ipAddress;
    private String userAgent;
    private Long instanceId;
    private Long instanceStageId;
    private Long stageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActionBy() {
        return actionBy;
    }

    public void setActionBy(Long actionBy) {
        this.actionBy = actionBy;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Instant getActionDate() {
        return actionDate;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }
}
