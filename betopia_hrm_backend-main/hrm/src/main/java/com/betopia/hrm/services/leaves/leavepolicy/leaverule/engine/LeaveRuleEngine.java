package com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveRuleEngine {

    private final List<RequestRule> requestRules;
    private final List<BalanceRule> balanceRules;
    private final List<EligibilityRule> eligibilityRules;

    public LeaveRuleEngine(
            List<RequestRule> requestRules,
            List<BalanceRule> balanceRules,
            List<EligibilityRule> eligibilityRules
    ) {
        this.requestRules = requestRules;
        this.balanceRules = balanceRules;
        this.eligibilityRules = eligibilityRules;
    }

    public ValidationResult validateRequest(Employee employee, LeaveRequest request, LeavePolicy policy) {
        for (LeaveRule rule : requestRules) {
            ValidationResult result = rule.validate(employee, request, policy);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.ok();
    }

    public ValidationResult validateBalance(Employee employee, LeaveRequest request, LeavePolicy policy) {
        for (LeaveRule rule : balanceRules) {
            ValidationResult result = rule.validate(employee, request, policy);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.ok();
    }

    public ValidationResult validateEligibility(Employee employee, LeaveRequest request, LeavePolicy policy) {
        for (LeaveRule rule : eligibilityRules) {
            ValidationResult result = rule.validate(employee, request, policy);
            if (!result.isValid()) {
                return result;
            }
        }
        return ValidationResult.ok();
    }
}
