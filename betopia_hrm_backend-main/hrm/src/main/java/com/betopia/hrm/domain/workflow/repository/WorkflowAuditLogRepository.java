package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.WorkflowAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowAuditLogRepository extends JpaRepository<WorkflowAuditLog, Long> {
}
