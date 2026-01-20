package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class CoveringEmployeeRule implements RequestRule {

    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (Boolean.TRUE.equals(policy.getCoveringEmployeeRequired())) {
            if (request.getCoveringEmployeeId() == null) {
                return ValidationResult.fail(
                        "COVERING_EMPLOYEE_REQUIRED",
                        "A covering employee must be assigned for " + policy.getLeaveType().getName()
                );
            }
        }
        return ValidationResult.ok();
    }
}
