package com.betopia.hrm.domain.leave.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.entity.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "leave_notifications")
public class LeaveNotifications extends Auditable<Long, Long> {

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
    @JoinColumn(name = "leave_approval_id")
    private LeaveApprovals leaveApproval;

    // Type of notification (e.g., LEAVE_REQUEST_SUBMITTED, LEAVE_APPROVED, etc.)
    @Column(name = "type", nullable = false, length = 50)
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

    public LeaveApprovals getLeaveApproval() {
        return leaveApproval;
    }

    public void setLeaveApproval(LeaveApprovals leaveApproval) {
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

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

}
