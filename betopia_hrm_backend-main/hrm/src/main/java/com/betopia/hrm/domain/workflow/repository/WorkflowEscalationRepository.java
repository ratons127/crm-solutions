package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.WorkflowEscalation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowEscalationRepository extends JpaRepository<WorkflowEscalation, Long> {
}
