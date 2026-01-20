package com.betopia.hrm.domain.leave.request;

import java.time.LocalDateTime;

public record LeaveNotificationsRequest(
        Long recipientId,
        Long senderId,
        Long leaveRequestId,
        Long leaveApprovalId,
        String type,
        String message,
        String notificationStatus// Optional: UNREAD, READ, etc.
) {
}
