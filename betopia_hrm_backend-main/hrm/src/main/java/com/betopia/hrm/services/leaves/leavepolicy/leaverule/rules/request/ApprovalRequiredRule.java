package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.enums.LeaveStatus;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class ApprovalRequiredRule implements RequestRule {

    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (Boolean.TRUE.equals(policy.getRequiresApproval())) {
            // leave request goes into "PENDING" state
            request.setStatus(LeaveStatus.PENDING);
            return ValidationResult.ok();   //"Leave request requires approval and is pending."
        } else {
            // auto-approve if no approval required
            request.setStatus(LeaveStatus.APPROVED);
            return ValidationResult.ok();   //"Leave request is auto-approved."
        }
    }
}
