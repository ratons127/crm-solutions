package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkflowStageRepository extends JpaRepository<WorkflowStage, Long> {

    List<WorkflowStage> findByTemplateIdOrderByStageOrderAsc(Long templateId);

    Optional<WorkflowStage> findByTemplateIdAndStageOrder(Long templateId, int stageOrder);
}
