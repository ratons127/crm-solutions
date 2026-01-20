package com.betopia.hrm.domain.cashadvance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "advance_salary_config")
public class AdvanceCashConfig  extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "minimum_woking_days")
    private Integer minimumWorkingDays;


    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_type_id", updatable = false)
    private EmployeeType employeeType;

    @Column(name = "advance_limit_percent")
    private BigDecimal advanceLimitPercent;

    @Column(name = "advance_limit_amount")
    private BigDecimal advanceLimitAmount;

    @Column(name = "service_charge_percent")
    private BigDecimal serviceChargePercent;

    @Column(name = "service_charge_amount")
    private BigDecimal serviceChargeAmount;
    @Column(name = "is_approved_amount_change")
    private Boolean isApprovedAmountChange;
    @Column(name = "status")
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinimumWorkingDays() {
        return minimumWorkingDays;
    }

    public void setMinimumWorkingDays(Integer minimumWorkingDays) {
        this.minimumWorkingDays = minimumWorkingDays;
    }


    public BigDecimal getAdvanceLimitPercent() {
        return advanceLimitPercent;
    }

    public void setAdvanceLimitPercent(BigDecimal advanceLimitPercent) {
        this.advanceLimitPercent = advanceLimitPercent;
    }

    public BigDecimal getAdvanceLimitAmount() {
        return advanceLimitAmount;
    }

    public void setAdvanceLimitAmount(BigDecimal advanceLimitAmount) {
        this.advanceLimitAmount = advanceLimitAmount;
    }

    public BigDecimal getServiceChargePercent() {
        return serviceChargePercent;
    }

    public void setServiceChargePercent(BigDecimal serviceChargePercent) {
        this.serviceChargePercent = serviceChargePercent;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(BigDecimal serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public Boolean getApprovedAmountChange() {
        return isApprovedAmountChange;
    }

    public void setApprovedAmountChange(Boolean approvedAmountChange) {
        isApprovedAmountChange = approvedAmountChange;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }
}
