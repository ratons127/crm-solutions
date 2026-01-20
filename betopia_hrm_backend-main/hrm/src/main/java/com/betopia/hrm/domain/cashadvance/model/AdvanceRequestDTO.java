package com.betopia.hrm.domain.cashadvance.model;

import java.math.BigDecimal;

public class AdvanceRequestDTO {

    private BigDecimal applicableaAdvanceAmount;
    private BigDecimal requestedAmount;
    private BigDecimal deductedAmount;
    private BigDecimal serviceCharge;


    public BigDecimal getApplicableaAdvanceAmount() {
        return applicableaAdvanceAmount;
    }

    public void setApplicableaAdvanceAmount(BigDecimal applicableaAdvanceAmount) {
        this.applicableaAdvanceAmount = applicableaAdvanceAmount;
    }

    public BigDecimal getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(BigDecimal deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
}
