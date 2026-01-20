package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.ApprovalWorkflows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalWorkflowsRepository extends JpaRepository<ApprovalWorkflows, Long> {
}
