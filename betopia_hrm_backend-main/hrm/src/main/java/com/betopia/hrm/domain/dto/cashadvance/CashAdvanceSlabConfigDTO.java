package com.betopia.hrm.domain.dto.cashadvance;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfigDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class CashAdvanceSlabConfigDTO {

    Long id;
    Integer advanceRequestDay;
    Long employeeTypeId;
    Long companyId;
    Long businessUnitId;
    Long workplaceId;
    Long workplaceGroupId;
    LocalDate effectiveFromDate;
    LocalDate effectiveToDate;
    String remarks;
    Boolean isApprovedAmountChange;
    Boolean status;

    BigDecimal advancePercent;
    String serviceChargeType;
    BigDecimal serviceChargeAmount;
    String setupName;

    List<CashAdvanceSlabConfigDetails> cashAdvanceSlabConfigDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAdvanceRequestDay() {
        return advanceRequestDay;
    }

    public void setAdvanceRequestDay(Integer advanceRequestDay) {
        this.advanceRequestDay = advanceRequestDay;
    }

    public Long getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Long employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }

    public Long getWorkplaceGroupId() {
        return workplaceGroupId;
    }

    public void setWorkplaceGroupId(Long workplaceGroupId) {
        this.workplaceGroupId = workplaceGroupId;
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

    public List<CashAdvanceSlabConfigDetails> getCashAdvanceSlabConfigDetails() {
        return cashAdvanceSlabConfigDetails;
    }

    public void setCashAdvanceSlabConfigDetails(List<CashAdvanceSlabConfigDetails> cashAdvanceSlabConfigDetails) {
        this.cashAdvanceSlabConfigDetails = cashAdvanceSlabConfigDetails;
    }

    public LocalDate getEffectiveFromDate() {
        return effectiveFromDate;
    }

    public void setEffectiveFromDate(LocalDate effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
    }

    public LocalDate getEffectiveToDate() {
        return effectiveToDate;
    }

    public void setEffectiveToDate(LocalDate effectiveToDate) {
        this.effectiveToDate = effectiveToDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getAdvancePercent() {
        return advancePercent;
    }

    public void setAdvancePercent(BigDecimal advancePercent) {
        this.advancePercent = advancePercent;
    }

    public String getServiceChargeType() {
        return serviceChargeType;
    }

    public void setServiceChargeType(String serviceChargeType) {
        this.serviceChargeType = serviceChargeType;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(BigDecimal serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public String getSetupName() {
        return setupName;
    }

    public void setSetupName(String setupName) {
        this.setupName = setupName;
    }
}
