package com.betopia.hrm.services.workflow.resolver;

import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.enums.ApproverType;

import java.util.Set;

public interface ApproverResolverStrategy {
    ApproverType getType();  // e.g. ROLE, DEPARTMENT_HEAD, etc.
    Set<Long> resolveApprovers(Long requesterId, StageApprover approverDef);
}
