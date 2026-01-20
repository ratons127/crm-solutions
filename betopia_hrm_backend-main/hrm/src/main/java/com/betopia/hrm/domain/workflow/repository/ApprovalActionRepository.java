package com.betopia.hrm.domain.workflow.repository;

import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalActionRepository extends JpaRepository<ApprovalAction, Long> {
}
