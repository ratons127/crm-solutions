package com.betopia.hrm.domain.dto.leave;

import java.time.LocalDateTime;

public class LeaveNotificationsDTO {

    private Long id;
    private Long recipient;
    private Long sender;
    private String senderName;
    private LeaveApprovalsDTO leaveApproval;
    private String type;
    private String message;
    private String status;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipient() {
        return recipient;
    }

    public void setRecipient(Long recipient) {
        this.recipient = recipient;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public LeaveApprovalsDTO getLeaveApproval() {
        return leaveApproval;
    }

    public void setLeaveApproval(LeaveApprovalsDTO leaveApproval) {
        this.leaveApproval = leaveApproval;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
