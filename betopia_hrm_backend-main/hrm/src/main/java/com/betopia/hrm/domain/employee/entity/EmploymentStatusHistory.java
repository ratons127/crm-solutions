package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.annotation.ExposeField;
import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.users.entity.Company;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "employment_status_history")
public class EmploymentStatusHistory extends Auditable<Long, Long> {

    @ExposeField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExposeField
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ExposeField
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;

    @ExposeField
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private EmploymentStatus status;

    @ExposeField
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @ExposeField
    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @ExposeField
    @Column(name = "reason_code", length = 100)
    private String reasonCode;

    @ExposeField
    @Column(columnDefinition = "TEXT")
    private String remarks;


    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public EmploymentStatus getStatus() { return status; }
    public void setStatus(EmploymentStatus status) { this.status = status; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }

    public String getReasonCode() { return reasonCode; }
    public void setReasonCode(String reasonCode) { this.reasonCode = reasonCode; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
