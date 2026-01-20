package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.employee.enums.ApprovalStatus;
import com.betopia.hrm.domain.employee.enums.RequestType;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.entity.Workplace;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_requests")
public class TransferRequests extends Auditable<Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestType requestType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_company_id", nullable = false)
    private Company fromCompanyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_company_id")
    private Company toCompanyId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_workplace_id", nullable = false)
    private Workplace fromWorkplaceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_workplace_id")
    private Workplace toWorkplaceId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_department_id", nullable = false)
    private Department fromDepartmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_department_id")
    private Department toDepartmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "from_designation_id", nullable = false)
    private Designation fromDesignationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_designation_id")
    private Designation toDesignationId;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private ApprovalStatus approvalStatus ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;


    @Column(name = "status")
    private Boolean status;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    // ---------------- Getters and Setters ----------------

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

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Company getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(Company fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public Company getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(Company toCompanyId) {
        this.toCompanyId = toCompanyId;
    }

    public Workplace getFromWorkplaceId() {
        return fromWorkplaceId;
    }

    public void setFromWorkplaceId(Workplace fromWorkplaceId) {
        this.fromWorkplaceId = fromWorkplaceId;
    }

    public Workplace getToWorkplaceId() {
        return toWorkplaceId;
    }

    public void setToWorkplaceId(Workplace toWorkplaceId) {
        this.toWorkplaceId = toWorkplaceId;
    }

    public Department getFromDepartmentId() {
        return fromDepartmentId;
    }

    public void setFromDepartmentId(Department fromDepartmentId) {
        this.fromDepartmentId = fromDepartmentId;
    }

    public Department getToDepartmentId() {
        return toDepartmentId;
    }

    public void setToDepartmentId(Department toDepartmentId) {
        this.toDepartmentId = toDepartmentId;
    }

    public Designation getFromDesignationId() {
        return fromDesignationId;
    }

    public void setFromDesignationId(Designation fromDesignationId) {
        this.fromDesignationId = fromDesignationId;
    }

    public Designation getToDesignationId() {
        return toDesignationId;
    }

    public void setToDesignationId(Designation toDesignationId) {
        this.toDesignationId = toDesignationId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
