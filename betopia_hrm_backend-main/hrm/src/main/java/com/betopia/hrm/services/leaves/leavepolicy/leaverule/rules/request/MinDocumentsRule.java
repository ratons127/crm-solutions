package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class MinDocumentsRule implements RequestRule {

    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (Boolean.TRUE.equals(policy.getProofRequired())) {
            int minDocs = policy.getMinDocumentsRequired() != null ? policy.getMinDocumentsRequired() : 0;
            int submittedDocs = request.getProofDocumentPath() != null ? request.getProofDocumentPath().size() : 0;

            if (submittedDocs < minDocs) {
                return ValidationResult.fail(
                        "MIN_DOCUMENTS_NOT_MET",
                        "At least " + minDocs + " document(s) are required for this leave type."
                );
            }
        }
        return ValidationResult.ok();
    }
}
