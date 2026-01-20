package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class GenderRule implements RequestRule {
    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (Boolean.TRUE.equals(policy.getGenderRestricted())) {
            if (policy.getEligibleGender() != null &&
                !policy.getEligibleGender().equalsIgnoreCase(employee.getGender())) {
                return ValidationResult.fail(
                        "GENDER_NOT_ELIGIBLE",
                        "This leave type is restricted to " + policy.getEligibleGender()
                );
            }
        }
        return ValidationResult.ok();
    }
}
