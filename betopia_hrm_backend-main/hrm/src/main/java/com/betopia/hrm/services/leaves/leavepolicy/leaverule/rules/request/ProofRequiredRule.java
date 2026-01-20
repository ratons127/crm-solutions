package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProofRequiredRule implements RequestRule {
    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {

        if (Boolean.TRUE.equals(policy.getProofRequired())
                && policy.getProofThreshold() != null
                && request.getDaysRequested().compareTo(BigDecimal.valueOf(policy.getProofThreshold())) > 0) {

            if (request.getJustification() == null || request.getJustification().isBlank()) {
                return ValidationResult.fail("PROOF_REQUIRED",
                        "Justification or proof required for more than " + policy.getProofThreshold() + " days.");
            }
        }
        return ValidationResult.ok();
    }
}
