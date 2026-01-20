package com.betopia.hrm.domain.leave.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LeavePolicyRequest(
        Long id,

        @NotNull(message = "Leave Type ID cannot be null")
        @Positive(message = "Leave Type ID must be positive")
        Long leaveTypeId,

        @NotNull(message = "Leave group assign id cannot be null")
        @Positive(message = "Leave group assign id must be positive")
        Long leaveGroupAssignId,

        Long employeeTypeId,

        Integer defaultQuota,

        Double accrualRatePerMonth,

        Integer minDays,

        Integer maxDays,

        @NotNull(message = "Gender Restricted cannot be null")
        Boolean carryForwardAllowed,

        Integer carryForwardCap,

        @NotNull(message = "Gender Restricted cannot be null")
        Boolean encashable,

        @NotNull(message = "Proof Required cannot be null")
        Boolean proofRequired,

        Integer proofThreshold,

        Integer minDocumentsRequired,

        @NotNull(message = "Gender Restricted cannot be null")
        Boolean genderRestricted,

        String eligibleGender,

        Integer tenureRequired,

        Integer tenureRequiredDays,

        Integer maxOccurrences,

        @NotNull(message = "Requires Approval cannot be null")
        Boolean requiresApproval,

        @NotNull(message = "Expedited Approval cannot be null")
        Boolean expeditedApproval,

        @NotNull(message = "Linked To Overtime cannot be null")
        Boolean linkedToOvertime,

        Integer extraDaysAfterYears,

        boolean restrictBridgeLeave,

        Integer maxBridgeDays,

        boolean allowNegativeBalance,

        Integer maxAdvanceDays,

        boolean overlapAllowed,

        boolean justificationRequiredForOverlap,

        Boolean allowHalfDay,

        Boolean earnedLeave,

        Integer earnedAfterDays,

        Integer earnedLeaveDays,

        Boolean coveringEmployeeRequired,

        Boolean status,

        Boolean isPaid
) {
}
