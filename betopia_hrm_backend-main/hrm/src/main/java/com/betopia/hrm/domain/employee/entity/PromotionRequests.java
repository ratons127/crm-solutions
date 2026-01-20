package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.ApprovalStatus;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotion_requests")
public class PromotionRequests extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_grade_id")
    private Grade currentGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_grade_id")
    private Grade newGrade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_designation_id")
    private Designation currentDesignation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_designation_id")
    private Designation newDesignation;

    @Column(name = "appraisal_id")
    private Integer appraisal;

    @Column(columnDefinition = "TEXT")
    private String justification;

    @Column(name = "promotion_type", nullable = false)
    private String promotionType; // 'Promotion' or 'Demotion'

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "salary_change", precision = 10, scale = 2)
    private BigDecimal salaryChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", length = 30)
    private ApprovalStatus approvalStatus ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(length = 20)
    private Boolean status; // 'active' or 'inactive'

    // ------------------- Getters and Setters -------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Grade getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(Grade currentGrade) {
        this.currentGrade = currentGrade;
    }

    public Grade getNewGrade() {
        return newGrade;
    }

    public void setNewGrade(Grade newGrade) {
        this.newGrade = newGrade;
    }

    public Designation getCurrentDesignation() {
        return currentDesignation;
    }

    public void setCurrentDesignation(Designation currentDesignation) {
        this.currentDesignation = currentDesignation;
    }

    public Designation getNewDesignation() {
        return newDesignation;
    }

    public void setNewDesignation(Designation newDesignation) {
        this.newDesignation = newDesignation;
    }

    public Integer getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(Integer appraisal) {
        this.appraisal = appraisal;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getSalaryChange() {
        return salaryChange;
    }

    public void setSalaryChange(BigDecimal salaryChange) {
        this.salaryChange = salaryChange;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
