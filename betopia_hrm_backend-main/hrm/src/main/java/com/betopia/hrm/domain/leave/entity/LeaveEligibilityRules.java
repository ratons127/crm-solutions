package com.betopia.hrm.domain.leave.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "leave_eligibility_rules")
public class LeaveEligibilityRules extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private LeaveGroup leaveGroup;

    // Attributes
    @Column(name = "gender", length = 10)
    private String gender; // MALE, FEMALE, ANY

    @Column(name = "min_tenure_months")
    private Integer minTenureMonths;

    @Column(name = "max_tenure_months")
    private Integer maxTenureMonths;

    @Column(name = "employment_status", length = 50)
    private String employmentStatus; // Permanent, Contract, Intern

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // --- Getters & Setters ---
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

    public LeaveGroup getLeaveGroup() {
        return leaveGroup;
    }

    public void setLeaveGroup(LeaveGroup leaveGroup) {
        this.leaveGroup = leaveGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getMinTenureMonths() {
        return minTenureMonths;
    }

    public void setMinTenureMonths(Integer minTenureMonths) {
        this.minTenureMonths = minTenureMonths;
    }

    public Integer getMaxTenureMonths() {
        return maxTenureMonths;
    }

    public void setMaxTenureMonths(Integer maxTenureMonths) {
        this.maxTenureMonths = maxTenureMonths;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
