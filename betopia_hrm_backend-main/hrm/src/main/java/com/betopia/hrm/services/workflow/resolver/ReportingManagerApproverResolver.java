package com.betopia.hrm.services.workflow.resolver;

import com.betopia.hrm.domain.workflow.entity.StageApprover;
import com.betopia.hrm.domain.workflow.enums.ApproverType;
import com.betopia.hrm.services.employee.EmployeeService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ReportingManagerApproverResolver implements ApproverResolverStrategy {

    private final EmployeeService employeeService;

    public ReportingManagerApproverResolver(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ApproverType getType() {
        return ApproverType.REPORTING_MANAGER;
    }

    @Override
    public Set<Long> resolveApprovers(Long requesterId, StageApprover approverDef) {
        Long managerId = employeeService.findReportingManagerId(requesterId);
        return managerId != null ? Set.of(managerId) : Set.of();
    }
}
