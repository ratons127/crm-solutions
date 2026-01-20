package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.PaymentMethod;
import com.betopia.hrm.domain.employee.enums.SettlementStatus;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "final_settlement ")
public class FinalSettlement extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_id")
    private EmployeeSeparations separation;

    @Column(name = "unpaid_salary", precision = 10, scale = 2)
    private BigDecimal unpaidSalary = BigDecimal.ZERO;

    @Column(name = "salary_arrears", precision = 10, scale = 2)
    private BigDecimal salaryArrears = BigDecimal.ZERO;

    @Column(name = "overtime_dues", precision = 10, scale = 2)
    private BigDecimal overtimeDues = BigDecimal.ZERO;

    @Column(name = "leave_encashment", precision = 10, scale = 2)
    private BigDecimal leaveEncashment = BigDecimal.ZERO;

    @Column(name = "bonus_dues", precision = 10, scale = 2)
    private BigDecimal bonusDues = BigDecimal.ZERO;

    @Column(name = "other_earnings", precision = 10, scale = 2)
    private BigDecimal otherEarnings = BigDecimal.ZERO;

    @Column(name = "total_earnings", precision = 10, scale = 2)
    private BigDecimal totalEarnings = BigDecimal.ZERO;

    // Deductions
    @Column(name = "notice_buyout", precision = 10, scale = 2)
    private BigDecimal noticeBuyout = BigDecimal.ZERO;

    @Column(name = "loan_recovery", precision = 10, scale = 2)
    private BigDecimal loanRecovery = BigDecimal.ZERO;

    @Column(name = "advance_recovery", precision = 10, scale = 2)
    private BigDecimal advanceRecovery = BigDecimal.ZERO;

    @Column(name = "tax_deduction", precision = 10, scale = 2)
    private BigDecimal taxDeduction = BigDecimal.ZERO;

    @Column(name = "statutory_deductions", precision = 10, scale = 2)
    private BigDecimal statutoryDeductions = BigDecimal.ZERO;

    @Column(name = "asset_loss_recovery", precision = 10, scale = 2)
    private BigDecimal assetLossRecovery = BigDecimal.ZERO;

    @Column(name = "other_deductions", precision = 10, scale = 2)
    private BigDecimal otherDeductions = BigDecimal.ZERO;

    @Column(name = "total_deductions", precision = 10, scale = 2)
    private BigDecimal totalDeductions = BigDecimal.ZERO;

    @Column(name = "net_payable", precision = 10, scale = 2)
    private BigDecimal netPayable = BigDecimal.ZERO;

    // Status & Approval
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_status", nullable = false)
    private SettlementStatus settlementStatus ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hr_approved_by")
    private User hrApprovedBy;

    @Column(name = "hr_approved_date")
    private LocalDateTime hrApprovedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "finance_approved_by")
    private User financeApprovedBy;

    @Column(name = "finance_approved_date")
    private LocalDateTime financeApprovedDate;

    // Disbursement
    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod ;

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Column(name = "voucher_number", length = 50)
    private String voucherNumber;

    @Column(name = "statement_path", length = 255)
    private String statementPath;

    @Column(name = "sla_deadline", nullable = false)
    private LocalDate slaDeadline;

    @Column(name = "is_overdue", nullable = false)
    private Boolean isOverdue = false;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EmployeeSeparations getSeparation() { return separation; }
    public void setSeparation(EmployeeSeparations separation) { this.separation = separation; }

    public BigDecimal getUnpaidSalary() { return unpaidSalary; }
    public void setUnpaidSalary(BigDecimal unpaidSalary) { this.unpaidSalary = unpaidSalary; }

    public BigDecimal getSalaryArrears() { return salaryArrears; }
    public void setSalaryArrears(BigDecimal salaryArrears) { this.salaryArrears = salaryArrears; }

    public BigDecimal getOvertimeDues() { return overtimeDues; }
    public void setOvertimeDues(BigDecimal overtimeDues) { this.overtimeDues = overtimeDues; }

    public BigDecimal getLeaveEncashment() { return leaveEncashment; }
    public void setLeaveEncashment(BigDecimal leaveEncashment) { this.leaveEncashment = leaveEncashment; }

    public BigDecimal getBonusDues() { return bonusDues; }
    public void setBonusDues(BigDecimal bonusDues) { this.bonusDues = bonusDues; }

    public BigDecimal getOtherEarnings() { return otherEarnings; }
    public void setOtherEarnings(BigDecimal otherEarnings) { this.otherEarnings = otherEarnings; }

    public BigDecimal getTotalEarnings() { return totalEarnings; }
    public void setTotalEarnings(BigDecimal totalEarnings) { this.totalEarnings = totalEarnings; }

    public BigDecimal getNoticeBuyout() { return noticeBuyout; }
    public void setNoticeBuyout(BigDecimal noticeBuyout) { this.noticeBuyout = noticeBuyout; }

    public BigDecimal getLoanRecovery() { return loanRecovery; }
    public void setLoanRecovery(BigDecimal loanRecovery) { this.loanRecovery = loanRecovery; }

    public BigDecimal getAdvanceRecovery() { return advanceRecovery; }
    public void setAdvanceRecovery(BigDecimal advanceRecovery) { this.advanceRecovery = advanceRecovery; }

    public BigDecimal getTaxDeduction() { return taxDeduction; }
    public void setTaxDeduction(BigDecimal taxDeduction) { this.taxDeduction = taxDeduction; }

    public BigDecimal getStatutoryDeductions() { return statutoryDeductions; }
    public void setStatutoryDeductions(BigDecimal statutoryDeductions) { this.statutoryDeductions = statutoryDeductions; }

    public BigDecimal getAssetLossRecovery() { return assetLossRecovery; }
    public void setAssetLossRecovery(BigDecimal assetLossRecovery) { this.assetLossRecovery = assetLossRecovery; }

    public BigDecimal getOtherDeductions() { return otherDeductions; }
    public void setOtherDeductions(BigDecimal otherDeductions) { this.otherDeductions = otherDeductions; }

    public BigDecimal getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(BigDecimal totalDeductions) { this.totalDeductions = totalDeductions; }

    public BigDecimal getNetPayable() { return netPayable; }
    public void setNetPayable(BigDecimal netPayable) { this.netPayable = netPayable; }

    public SettlementStatus getSettlementStatus() { return settlementStatus; }
    public void setSettlementStatus(SettlementStatus settlementStatus) { this.settlementStatus = settlementStatus; }

    public User getHrApprovedBy() { return hrApprovedBy; }
    public void setHrApprovedBy(User hrApprovedBy) { this.hrApprovedBy = hrApprovedBy; }

    public LocalDateTime getHrApprovedDate() { return hrApprovedDate; }
    public void setHrApprovedDate(LocalDateTime hrApprovedDate) { this.hrApprovedDate = hrApprovedDate; }

    public User getFinanceApprovedBy() { return financeApprovedBy; }
    public void setFinanceApprovedBy(User financeApprovedBy) { this.financeApprovedBy = financeApprovedBy; }

    public LocalDateTime getFinanceApprovedDate() { return financeApprovedDate; }
    public void setFinanceApprovedDate(LocalDateTime financeApprovedDate) { this.financeApprovedDate = financeApprovedDate; }

    public LocalDate getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDate disbursementDate) { this.disbursementDate = disbursementDate; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }

    public String getVoucherNumber() { return voucherNumber; }
    public void setVoucherNumber(String voucherNumber) { this.voucherNumber = voucherNumber; }

    public String getStatementPath() { return statementPath; }
    public void setStatementPath(String statementPath) { this.statementPath = statementPath; }

    public LocalDate getSlaDeadline() { return slaDeadline; }
    public void setSlaDeadline(LocalDate slaDeadline) { this.slaDeadline = slaDeadline; }

    public Boolean getIsOverdue() { return isOverdue; }
    public void setIsOverdue(Boolean isOverdue) { this.isOverdue = isOverdue; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
