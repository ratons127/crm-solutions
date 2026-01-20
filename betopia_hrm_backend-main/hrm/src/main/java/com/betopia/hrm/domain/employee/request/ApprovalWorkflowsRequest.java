package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.Modules;

public record ApprovalWorkflowsRequest(
        Modules module,
        Integer level,
        Long approverRoleId,
        Long approverId,
        Integer slaHours,
        Long escalationToRoleId,
        Boolean status
) {
}
