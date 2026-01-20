package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.employee.enums.ChangeType;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "employee_assignment_history")
public class EmployeeAssignmentHistory extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type")
    private ChangeType changeType;

    @ManyToOne
    @JoinColumn(name = "from_department_id")
    private Department fromDepartment;

    @ManyToOne
    @JoinColumn(name = "to_department_id")
    private Department toDepartment;

    @ManyToOne
    @JoinColumn(name = "from_designation_id")
    private Designation fromDesignation;

    @ManyToOne
    @JoinColumn(name = "to_designation_id")
    private Designation toDesignation;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "remarks")
    private String remarks;


    @Column(name = "status")
    private Boolean status ;

    // ===== Getters and Setters =====

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

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public Department getFromDepartment() {
        return fromDepartment;
    }

    public void setFromDepartment(Department fromDepartment) {
        this.fromDepartment = fromDepartment;
    }

    public Department getToDepartment() {
        return toDepartment;
    }

    public void setToDepartment(Department toDepartment) {
        this.toDepartment = toDepartment;
    }

    public Designation getFromDesignation() {
        return fromDesignation;
    }

    public void setFromDesignation(Designation fromDesignation) {
        this.fromDesignation = fromDesignation;
    }

    public Designation getToDesignation() {
        return toDesignation;
    }

    public void setToDesignation(Designation toDesignation) {
        this.toDesignation = toDesignation;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
