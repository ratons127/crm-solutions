package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.enums.NotificationType;

import java.time.Instant;

public record WorkflowNotificationRequest(
        Long id,
        NotificationType notificationType,
        Long recipientId,
        String subject,
        String message,
        Instant sentAt,
        Boolean isRead,
        Instant readAt,
        Long instanceId
) {
}
