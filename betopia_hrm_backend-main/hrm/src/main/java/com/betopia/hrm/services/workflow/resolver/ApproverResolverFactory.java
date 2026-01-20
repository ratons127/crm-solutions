package com.betopia.hrm.services.workflow.resolver;

import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.enums.ApproverType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ApproverResolverFactory {

    private final Map<ApproverType, ApproverResolverStrategy> resolverMap = new EnumMap<>(ApproverType.class);

    public ApproverResolverFactory(List<ApproverResolverStrategy> strategies) {
        for (ApproverResolverStrategy strategy : strategies) {
            resolverMap.put(strategy.getType(), strategy);
        }
    }

    public Set<Long> resolve(ApproverType type, Long requesterId, StageApprover approverDef) {
        ApproverResolverStrategy strategy = resolverMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No resolver found for type: " + type);
        }
        return strategy.resolveApprovers(requesterId, approverDef);
    }
}
