package com.betopia.hrm.services.workflow.resolver;

import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.enums.ApproverType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DepartmentHeadApproverResolver implements ApproverResolverStrategy{
    @Override
    public ApproverType getType() {
        return ApproverType.DEPARTMENT_HEAD;
    }

    @Override
    public Set<Long> resolveApprovers(Long requesterId, StageApprover approverDef) {
        return Set.of();
    }
}
