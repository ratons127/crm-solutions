package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MinDaysRule implements RequestRule {
    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (policy.getMinDays() != null
                && request.getDaysRequested().compareTo(BigDecimal.valueOf(policy.getMinDays())) < 0) {
            return ValidationResult.fail("MIN_DAYS",
                    "Minimum " + policy.getMinDays() + " days required.");
        }
        return ValidationResult.ok();
    }
}
