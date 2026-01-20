package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceAssignee;
import com.betopia.hrm.domain.workflow.enums.AssigneeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkflowInstanceAssigneeRepository extends JpaRepository<WorkflowInstanceAssignee, Long> {

    WorkflowInstanceAssignee findByInstanceStageIdAndUserId(Long instStageId, Long approverId);

    List<WorkflowInstanceAssignee> findByInstanceStageIdAndStatus(Long instStageId, AssigneeStatus assigneeStatus);

    List<WorkflowInstanceAssignee> findByInstanceStageId(Long instStageId);
}
