package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.eligibility;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.EligibilityRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class TenureRequiredRule implements EligibilityRule {

    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        long daysOfService = ChronoUnit.DAYS.between(employee.getDateOfJoining(), LocalDate.now());

        if (policy.getTenureRequiredDays() != null && daysOfService < policy.getTenureRequiredDays()) {
            return ValidationResult.fail("TENURE_REQUIRED_DAYS",
                    "Employee must complete at least " + policy.getTenureRequiredDays() + " days of service.");
        }

        return ValidationResult.ok();
    }
}
