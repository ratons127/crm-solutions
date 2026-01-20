package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.WorkflowNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowNotificationRepository extends JpaRepository<WorkflowNotification, Long> {
}
