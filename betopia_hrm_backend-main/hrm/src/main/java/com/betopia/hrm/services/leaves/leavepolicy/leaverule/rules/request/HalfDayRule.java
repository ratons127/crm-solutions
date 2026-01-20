package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HalfDayRule implements RequestRule {
    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (Boolean.TRUE.equals(request.getHalfDay())) {
            if (!Boolean.TRUE.equals(policy.getAllowHalfDay())) {
                return ValidationResult.fail(
                        "HALF_DAY_NOT_ALLOWED",
                        policy.getLeaveType().getName() + " does not allow half-day leaves"
                );
            }
            if (request.getDaysRequested().compareTo(BigDecimal.valueOf(0.5)) != 0) {
                return ValidationResult.fail(
                        "INVALID_HALF_DAY",
                        "Requested days must be 0.5 for half-day leave"
                );
            }
        }
        return ValidationResult.ok();
    }
}
