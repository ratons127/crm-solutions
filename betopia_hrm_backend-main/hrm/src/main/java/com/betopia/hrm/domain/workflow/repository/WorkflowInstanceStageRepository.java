package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowInstanceStageRepository extends JpaRepository<WorkflowInstanceStage, Long> {
}
