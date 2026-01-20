package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MaxDaysRule implements RequestRule {
    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (policy.getMaxDays() != null
                && policy.getMinDays() != null
                && BigDecimal.valueOf(policy.getMinDays()).compareTo(BigDecimal.ZERO) > 0
                && request.getDaysRequested().compareTo(BigDecimal.valueOf(policy.getMaxDays())) > 0) {
            return ValidationResult.fail("MAX_DAYS",
                    "Cannot request more than " + policy.getMaxDays() + " days.");
        }
        return ValidationResult.ok();
    }
}
