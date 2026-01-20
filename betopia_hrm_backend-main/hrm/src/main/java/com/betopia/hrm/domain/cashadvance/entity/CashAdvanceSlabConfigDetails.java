package com.betopia.hrm.domain.cashadvance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "cash_advance_slab_config_details")
public class CashAdvanceSlabConfigDetails extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name = "advance_amount_type")
    //private String advanceAmountType;
   // @Column(name = "advance_amount")
  //  private BigDecimal advanceAmount;
    @Column(name = "service_charge_type")
    private String serviceChargeType;
    @Column(name = "service_charge_amount")
    private BigDecimal serviceChargeAmount;
    @Column(name = "from_amount")
    private BigDecimal fromAmount;
    @Column(name = "to_amount")
    private BigDecimal toAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cash_advance_slab_config_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CashAdvanceSlabConfig  cashAdvanceSlabConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public CashAdvanceSlabConfig getCashAdvanceSlabConfig() {
        return cashAdvanceSlabConfig;
    }

    public void setCashAdvanceSlabConfig(CashAdvanceSlabConfig cashAdvanceSlabConfig) {
        this.cashAdvanceSlabConfig = cashAdvanceSlabConfig;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }
}
