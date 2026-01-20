package com.betopia.hrm.domain.leave.mapper;

import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.repository.LeaveGroupAssignRepository;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRepository;
import com.betopia.hrm.domain.leave.request.LeavePolicyRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ManualLeavePolicyMapper {

    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveGroupAssignRepository leaveGroupAssignRepository;

    public ManualLeavePolicyMapper(LeaveTypeRepository leaveTypeRepository,
                                   LeaveGroupAssignRepository leaveGroupAssignRepository
    ) {
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveGroupAssignRepository = leaveGroupAssignRepository;
    }

    /**
     * Map CreateRequest DTO -> Entity
     */
    public LeavePolicy toEntity(LeavePolicyRequest request) {
        LeavePolicy leavePolicy = new LeavePolicy();
        updateEntity(leavePolicy, request);
        return leavePolicy;
    }

    /**
     * Update Entity fields from CreateRequest
     */
    public void updateEntity(LeavePolicy entity, LeavePolicyRequest request) {
        LeaveType leaveType = leaveTypeRepository.findById(request.leaveTypeId())
                .orElseThrow(() -> new EntityNotFoundException("LeaveType not found with id: " + request.leaveTypeId()));

        LeaveGroupAssign leaveGroupAssign = leaveGroupAssignRepository.findById(request.leaveGroupAssignId())
                .orElseThrow(() -> new EntityNotFoundException("LeaveGroupAssign not found with id: " + request.leaveGroupAssignId()));

        entity.setLeaveType(leaveType);
        entity.setLeaveGroupAssign(leaveGroupAssign);
        entity.setEmployeeTypeId(request.employeeTypeId());
        entity.setDefaultQuota(request.defaultQuota());
        entity.setAccrualRatePerMonth(request.accrualRatePerMonth());
        entity.setMinDays(request.minDays());
        entity.setMaxDays(request.maxDays());
        entity.setCarryForwardAllowed(request.carryForwardAllowed());
        entity.setCarryForwardCap(request.carryForwardCap());
        entity.setEncashable(request.encashable());
        entity.setProofRequired(request.proofRequired());
        entity.setProofThreshold(request.proofThreshold());
        entity.setMinDocumentsRequired(request.minDocumentsRequired());
        entity.setGenderRestricted(request.genderRestricted());
        entity.setEligibleGender(request.eligibleGender());
        entity.setTenureRequired(request.tenureRequired());
        entity.setTenureRequiredDays(request.tenureRequiredDays());
        entity.setMaxOccurrences(request.maxOccurrences());
        entity.setRequiresApproval(request.requiresApproval());
        entity.setExpeditedApproval(request.expeditedApproval());
        entity.setLinkedToOvertime(request.linkedToOvertime());
        entity.setExtraDaysAfterYears(request.extraDaysAfterYears());
        entity.setRestrictBridgeLeave(request.restrictBridgeLeave());
        entity.setMaxBridgeDays(request.maxBridgeDays());
        entity.setAllowNegativeBalance(request.allowNegativeBalance());
        entity.setMaxAdvanceDays(request.maxAdvanceDays());
        entity.setOverlapAllowed(request.overlapAllowed());
        entity.setJustificationRequiredForOverlap(request.justificationRequiredForOverlap());
        entity.setAllowHalfDay(request.allowHalfDay());
        entity.setEarnedLeave(request.earnedLeave());
        entity.setEarnedAfterDays(request.earnedAfterDays());
        entity.setEarnedLeaveDays(request.earnedLeaveDays());
        entity.setStatus(request.status());
        entity.setIsPaid(request.isPaid());
    }
}
