package com.betopia.hrm.domain.dto.cashadvance;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AdvanceCashConfigDTO {

    private Long id;

    private Integer minimumWorkingDays;

    private Long employeeTypeId;

    private BigDecimal advanceLimitPercent;

    private BigDecimal advanceLimitAmount;

    private BigDecimal serviceChargePercent;

    private BigDecimal serviceChargeAmount;

    private Boolean isApprovedAmountChange;

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

    public Long getEmployeeTypeId() {
        return employeeTypeId;
    }

    public void setEmployeeTypeId(Long employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }
}
