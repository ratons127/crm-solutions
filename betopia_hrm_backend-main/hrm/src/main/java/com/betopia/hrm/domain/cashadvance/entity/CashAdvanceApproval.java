package com.betopia.hrm.domain.cashadvance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.cashadvance.enums.CashAdvanceStatus;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

import java.time.LocalDateTime;


@Entity
@Table(name="cash_advance_approvals")
public class CashAdvanceApproval extends Auditable<Long,Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "advance_salary_request_id", nullable = false)
    private AdvanceCashRequest advanceCashRequest;

    // The approver (employee)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "approver_id", nullable = false)
    private Long approverId;

    @Column(name = "level", nullable = false)
    private Integer level;


    @Column(name = "status")
    private String status;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdvanceCashRequest getAdvanceCashRequest() {
        return advanceCashRequest;
    }

    public void setAdvanceCashRequest(AdvanceCashRequest advanceCashRequest) {
        this.advanceCashRequest = advanceCashRequest;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
