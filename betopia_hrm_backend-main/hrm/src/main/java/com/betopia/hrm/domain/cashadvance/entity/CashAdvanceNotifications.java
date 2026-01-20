package com.betopia.hrm.domain.cashadvance.entity;

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

@Entity
@Table(name="cash_advance_notifications")
public class CashAdvanceNotifications extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The employee who receives the notification (e.g., supervisor or requester)
    @Column(name = "recipient_id")
    private Long recipient;

    // The employee who triggered the notification (e.g., requester or approver)
    @Column(name = "sender_id")
    private Long sender;

    // Optional link to specific approval action
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_advance_approval_id")
    private CashAdvanceApproval cashAdvanceApproval;

    // Type of notification (e.g., REQUEST_SUBMITTED, APPROVED, etc.)
    @Column(name = "type", nullable = false)
    private String type;

    // Notification message text
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    // Notification read status
    @Column(name = "notification_status")
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

    public CashAdvanceApproval getCashAdvanceApproval() {
        return cashAdvanceApproval;
    }

    public void setCashAdvanceApproval(CashAdvanceApproval cashAdvanceApproval) {
        this.cashAdvanceApproval = cashAdvanceApproval;
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
