package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;


import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.repository.LeaveRequestRepository;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OverlapRule implements RequestRule {

    private final LeaveRequestRepository leaveRequestRepository;

    public OverlapRule(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        List<LeaveRequest> overlaps;

        //LeaveStatus.APPROVED
        if (request.getId() != null) {
            overlaps = leaveRequestRepository.findOverlapsExcludingSelf(
                    request.getEmployeeId(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getId()
            );
        } else {
            overlaps = leaveRequestRepository.findOverlaps(
                    request.getEmployeeId(),
                    request.getStartDate(),
                    request.getEndDate()
            );
        }

        if (!overlaps.isEmpty()) {
            if (policy.isOverlapAllowed()) {
                // overlap allowed but justification may be required
                if (policy.isJustificationRequiredForOverlap()
                        && (request.getJustification() == null || request.getJustification().isBlank())) {
                    return ValidationResult.fail(
                            "JUSTIFICATION_REQUIRED",
                            "Overlap detected. Please provide justification."
                    );
                }
                return ValidationResult.ok();
            }

            // overlap not allowed
            return ValidationResult.fail(
                    "OVERLAP_NOT_ALLOWED",
                    "Leave overlaps with existing request from " +
                            overlaps.get(0).getStartDate() + " to " + overlaps.get(0).getEndDate()
            );
        }

        return ValidationResult.ok();
    }
}
