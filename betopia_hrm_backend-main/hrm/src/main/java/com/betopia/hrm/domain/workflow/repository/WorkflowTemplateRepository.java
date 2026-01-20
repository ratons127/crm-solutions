package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate, Long> {
}
