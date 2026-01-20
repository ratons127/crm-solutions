package com.betopia.hrm.services.workflow.resolver;

import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.enums.ApproverType;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RoleApproverResolver implements ApproverResolverStrategy{
    @Override
    public ApproverType getType() {
        return ApproverType.ROLE;
    }

    @Override
    public Set<Long> resolveApprovers(Long requesterId, StageApprover approverDef) {
        return Set.of();
    }
}
