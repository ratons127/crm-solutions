package com.betopia.hrm.domain.leave.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "leave_policies")
public class LeavePolicy extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_group_assign_id", nullable = false)
    private LeaveGroupAssign leaveGroupAssign;

    @Column(name = "employee_type_id")
    private Long employeeTypeId;

    @Column(name = "default_quota")
    private Integer defaultQuota;  // e.g., 18 annual, 12 casual

    @Column(name = "accrual_rate_per_month")
    private Double accrualRatePerMonth;     // e.g., 1.5 for annual leave

    @Column(name = "min_days")
    private Integer minDays;

    @Column(name = "max_days")
    private Integer maxDays;

    @Column(name = "carry_forward_allowed")
    private Boolean carryForwardAllowed;

    @Column(name = "carry_forward_cap")
    private Integer carryForwardCap;

    @Column(name = "encashable")
    private Boolean encashable;

    @Column(name = "proof_required")
    private Boolean proofRequired;

    @Column(name = "proof_threshold")
    private Integer proofThreshold;

    @Column(name = "min_documents_required")
    private Integer minDocumentsRequired; // e.g., 1 document required

    @Column(name = "gender_restricted")
    private Boolean genderRestricted;

    @Column(name = "eligible_gender")
    private String eligibleGender; // MALE / FEMALE

    @Column(name = "tenure_required")
    private Integer tenureRequired; // months/years; min tenure before leave allowed

    @Column(name = "tenure_required_days")
    private Integer tenureRequiredDays; // days; min tenure before leave allowed

    @Column(name = "max_occurrences")
    private Integer maxOccurrences;

    // --- Special Scenario: Service Length Dependency ---
    @Column(name = "extra_days_after_years")
    private Integer extraDaysAfterYears;    // additional entitlement (e.g., +6 after 5 years)

    // --- Special Scenario: Bridge Leave ---
    @Column(name = "restrict_bridge_leave")
    private boolean restrictBridgeLeave;    // true = restrict long weekend leaves

    @Column(name = "max_bridge_days")
    private Integer maxBridgeDays;          // max allowed if bridging holidays/weekends

    // --- Special Scenario: Negative Balance (Advance Leave) ---
    @Column(name = "allow_negative_balance")
    private boolean allowNegativeBalance;   // true = advance leave allowed

    @Column(name = "max_advance_days")
    private Integer maxAdvanceDays;         // how many days can be taken in advance

    @Column(name = "requires_approval")
    private Boolean requiresApproval;

    @Column(name = "expedited_approval")
    private Boolean expeditedApproval;

    @Column(name = "linked_to_overtime")
    private Boolean linkedToOvertime;

    @Column(name = "overlap_allowed")
    private boolean overlapAllowed;

    @Column(name = "justification_required_for_overlap")
    private boolean justificationRequiredForOverlap;

    @Column(name = "allow_half_day")
    private Boolean allowHalfDay; // NEW: true if half-day leave is allowed

    @Column(name = "earned_leave")
    private Boolean earnedLeave;              // true if this is earned leave type

    @Column(name = "earned_after_days")
    private Integer earnedAfterDays;          // e.g. 16 days worked

    @Column(name = "earned_leave_days")
    private Integer earnedLeaveDays;          // e.g. 1 leave earned after earnedAfterDays

    @Column(name = "covering_employee_required")
    private Boolean coveringEmployeeRequired;   // true if policy mandates covering employee

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "is_paid")
    private Boolean isPaid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveGroupAssign getLeaveGroupAssign() {
        return leaveGroupAssign;
    }

    public void setLeaveGroupAssign(LeaveGroupAssign leaveGroupAssign) {
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

    public Double getAccrualRatePerMonth() {
        return accrualRatePerMonth;
    }

    public void setAccrualRatePerMonth(Double accrualRatePerMonth) {
        this.accrualRatePerMonth = accrualRatePerMonth;
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

    public Boolean getIsPaid() {
        return isPaid;
    }

    public  void setIsPaid(Boolean isPaid){
        this.isPaid = isPaid;
    }
}
