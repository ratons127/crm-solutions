package com.betopia.hrm.domain.dto.leave;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class LeavePolicyDTO {

    private Long id;
    private LeaveTypeDTO  leaveType;
    private LeaveGroupAssignDTO leaveGroupAssign;
    private Long employeeTypeId;
    private Integer defaultQuota;  // e.g., 18 annual, 12 casual
    private Double accrualRatePerMonth;     // e.g., 1.5 for annual leave
    private Integer minDays;
    private Integer maxDays;
    private Boolean carryForwardAllowed;
    private Integer carryForwardCap;
    private Boolean encashable;
    private Boolean proofRequired;
    private Integer proofThreshold;
    private Integer minDocumentsRequired; // e.g., 1 document required
    private Boolean genderRestricted;
    private String eligibleGender; // MALE / FEMALE
    private Integer tenureRequired; // months/years; min tenure before leave allowed
    private Integer tenureRequiredDays; // days; min tenure before leave allowed
    private Integer maxOccurrences;
    private Integer extraDaysAfterYears;    // additional entitlement (e.g., +6 after 5 years)
    private boolean restrictBridgeLeave;    // true = restrict long weekend leaves
    private Integer maxBridgeDays;          // max allowed if bridging holidays/weekends
    private boolean allowNegativeBalance;   // true = advance leave allowed
    private Integer maxAdvanceDays;         // how many days can be taken in advance
    private Boolean requiresApproval;
    private Boolean expeditedApproval;
    private Boolean linkedToOvertime;
    private boolean overlapAllowed;
    private boolean justificationRequiredForOverlap;
    private Boolean allowHalfDay; // NEW: true if half-day leave is allowed
    private Boolean earnedLeave;              // true if this is earned leave type
    private Integer earnedAfterDays;          // e.g. 16 days worked
    private Integer earnedLeaveDays;          // e.g. 1 leave earned after earnedAfterDays
    private Boolean coveringEmployeeRequired;   // true if policy mandates covering employee
    private Boolean status = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveTypeDTO getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveTypeDTO leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveGroupAssignDTO getLeaveGroupAssign() {
        return leaveGroupAssign;
    }

    public void setLeaveGroupAssign(LeaveGroupAssignDTO leaveGroupAssign) {
        this.leaveGroupAssign = leaveGroupAssign;
    }

    public Long getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Long employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public Integer getDefaultQuota() {
        return defaultQuota;
    }

    public void setDefaultQuota(Integer defaultQuota) {
        this.defaultQuota = defaultQuota;
    }

    public Double getAccrualRatePerMonth() {
        return accrualRatePerMonth;
    }

    public void setAccrualRatePerMonth(Double accrualRatePerMonth) {
        this.accrualRatePerMonth = accrualRatePerMonth;
    }

    public Integer getMinDays() {
        return minDays;
    }

    public void setMinDays(Integer minDays) {
        this.minDays = minDays;
    }

    public Integer getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(Integer maxDays) {
        this.maxDays = maxDays;
    }

    public Boolean getCarryForwardAllowed() {
        return carryForwardAllowed;
    }

    public void setCarryForwardAllowed(Boolean carryForwardAllowed) {
        this.carryForwardAllowed = carryForwardAllowed;
    }

    public Integer getCarryForwardCap() {
        return carryForwardCap;
    }

    public void setCarryForwardCap(Integer carryForwardCap) {
        this.carryForwardCap = carryForwardCap;
    }

    public Boolean getEncashable() {
        return encashable;
    }

    public void setEncashable(Boolean encashable) {
        this.encashable = encashable;
    }

    public Boolean getProofRequired() {
        return proofRequired;
    }

    public void setProofRequired(Boolean proofRequired) {
        this.proofRequired = proofRequired;
    }

    public Integer getProofThreshold() {
        return proofThreshold;
    }

    public void setProofThreshold(Integer proofThreshold) {
        this.proofThreshold = proofThreshold;
    }

    public Integer getMinDocumentsRequired() {
        return minDocumentsRequired;
    }

    public void setMinDocumentsRequired(Integer minDocumentsRequired) {
        this.minDocumentsRequired = minDocumentsRequired;
    }

    public Boolean getGenderRestricted() {
        return genderRestricted;
    }

    public void setGenderRestricted(Boolean genderRestricted) {
        this.genderRestricted = genderRestricted;
    }

    public String getEligibleGender() {
        return eligibleGender;
    }

    public void setEligibleGender(String eligibleGender) {
        this.eligibleGender = eligibleGender;
    }

    public Integer getTenureRequired() {
        return tenureRequired;
    }

    public void setTenureRequired(Integer tenureRequired) {
        this.tenureRequired = tenureRequired;
    }

    public Integer getTenureRequiredDays() {
        return tenureRequiredDays;
    }

    public void setTenureRequiredDays(Integer tenureRequiredDays) {
        this.tenureRequiredDays = tenureRequiredDays;
    }

    public Integer getMaxOccurrences() {
        return maxOccurrences;
    }

    public void setMaxOccurrences(Integer maxOccurrences) {
        this.maxOccurrences = maxOccurrences;
    }

    public Integer getExtraDaysAfterYears() {
        return extraDaysAfterYears;
    }

    public void setExtraDaysAfterYears(Integer extraDaysAfterYears) {
        this.extraDaysAfterYears = extraDaysAfterYears;
    }

    public boolean isRestrictBridgeLeave() {
        return restrictBridgeLeave;
    }

    public void setRestrictBridgeLeave(boolean restrictBridgeLeave) {
        this.restrictBridgeLeave = restrictBridgeLeave;
    }

    public Integer getMaxBridgeDays() {
        return maxBridgeDays;
    }

    public void setMaxBridgeDays(Integer maxBridgeDays) {
        this.maxBridgeDays = maxBridgeDays;
    }

    public boolean isAllowNegativeBalance() {
        return allowNegativeBalance;
    }

    public void setAllowNegativeBalance(boolean allowNegativeBalance) {
        this.allowNegativeBalance = allowNegativeBalance;
    }

    public Integer getMaxAdvanceDays() {
        return maxAdvanceDays;
    }

    public void setMaxAdvanceDays(Integer maxAdvanceDays) {
        this.maxAdvanceDays = maxAdvanceDays;
    }

    public Boolean getRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public Boolean getExpeditedApproval() {
        return expeditedApproval;
    }

    public void setExpeditedApproval(Boolean expeditedApproval) {
        this.expeditedApproval = expeditedApproval;
    }

    public Boolean getLinkedToOvertime() {
        return linkedToOvertime;
    }

    public void setLinkedToOvertime(Boolean linkedToOvertime) {
        this.linkedToOvertime = linkedToOvertime;
    }

    public boolean isOverlapAllowed() {
        return overlapAllowed;
    }

    public void setOverlapAllowed(boolean overlapAllowed) {
        this.overlapAllowed = overlapAllowed;
    }

    public boolean isJustificationRequiredForOverlap() {
        return justificationRequiredForOverlap;
    }

    public void setJustificationRequiredForOverlap(boolean justificationRequiredForOverlap) {
        this.justificationRequiredForOverlap = justificationRequiredForOverlap;
    }

    public Boolean getAllowHalfDay() {
        return allowHalfDay;
    }

    public void setAllowHalfDay(Boolean allowHalfDay) {
        this.allowHalfDay = allowHalfDay;
    }

    public Boolean getEarnedLeave() {
        return earnedLeave;
    }

    public void setEarnedLeave(Boolean earnedLeave) {
        this.earnedLeave = earnedLeave;
    }

    public Integer getEarnedAfterDays() {
        return earnedAfterDays;
    }

    public void setEarnedAfterDays(Integer earnedAfterDays) {
        this.earnedAfterDays = earnedAfterDays;
    }

    public Integer getEarnedLeaveDays() {
        return earnedLeaveDays;
    }

    public void setEarnedLeaveDays(Integer earnedLeaveDays) {
        this.earnedLeaveDays = earnedLeaveDays;
    }

    public Boolean getCoveringEmployeeRequired() {
        return coveringEmployeeRequired;
    }

    public void setCoveringEmployeeRequired(Boolean coveringEmployeeRequired) {
        this.coveringEmployeeRequired = coveringEmployeeRequired;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
