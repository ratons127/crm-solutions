package com.betopia.hrm.domain.cashadvance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "cash_advance_slab_config")
public class CashAdvanceSlabConfig extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advance_request_day")
    private Integer advanceRequestDay;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_type_id", updatable = false)
    private EmployeeType employeeType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "company_id", updatable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "business_units_id", updatable = false)
    private BusinessUnit   businessUnit;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "workplace_group_id", updatable = false)
    private WorkplaceGroup workplaceGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "workplace_id", updatable = false)
    private Workplace workplace;

    @Column(name = "effective_from_date")
    private LocalDate effectiveFromDate;
    @Column(name = "effective_to_date")
    private LocalDate effectiveToDate;

    @Column(name = "advance_percent")
    private BigDecimal advancePercent;

    @Column(name = "service_charge_type")
    private String serviceChargeType;

    @Column(name = "service_charge_amount")
    private BigDecimal serviceChargeAmount;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "is_approved_amount_change")
    private Boolean isApprovedAmountChange;
    @Column(name = "status")
    private Boolean status;

    @Column(name = "setup_name")
    private String setupName;

    @OneToMany(mappedBy = "cashAdvanceSlabConfig", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<CashAdvanceSlabConfigDetails> cashAdvanceSlabConfigDetails=new ArrayList<>();

    public void addDetail(CashAdvanceSlabConfigDetails d) {
        cashAdvanceSlabConfigDetails.add(d);
        d.setCashAdvanceSlabConfig(this);   // <-- FK set here
    }
    public void removeDetail(CashAdvanceSlabConfigDetails d) {
        cashAdvanceSlabConfigDetails.remove(d);
        d.setCashAdvanceSlabConfig(null);
    }

    public void setCashAdvanceSlabConfigDetails(List<CashAdvanceSlabConfigDetails> items) {
        cashAdvanceSlabConfigDetails.clear();
        if (items != null) items.forEach(this::addDetail);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
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


    public Integer getAdvanceRequestDay() {
        return advanceRequestDay;
    }

    public void setAdvanceRequestDay(Integer advanceRequestDay) {
        this.advanceRequestDay = advanceRequestDay;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public WorkplaceGroup getWorkplaceGroup() {
        return workplaceGroup;
    }

    public void setWorkplaceGroup(WorkplaceGroup workplaceGroup) {
        this.workplaceGroup = workplaceGroup;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
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
