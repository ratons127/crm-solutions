package com.betopia.hrm.domain.dto.cashadvance;

public class CashAdvanceNotificationDTO {

    private Long id;

    private Long recipient;

    private Long sender;

    private Long cashAdvanceApprovalId;

    private String type;

    private String message;

    private String notificationStatus;

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

    public Long getCashAdvanceApprovalId() {
        return cashAdvanceApprovalId;
    }

    public void setCashAdvanceApprovalId(Long cashAdvanceApprovalId) {
        this.cashAdvanceApprovalId = cashAdvanceApprovalId;
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

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
