package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.user.UserDTO;
import com.betopia.hrm.domain.employee.enums.PaymentMethod;
import com.betopia.hrm.domain.employee.enums.SettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinalSettlementDTO {


    private Long id;
    private EmployeeSeparationsDTO separation;
    // Earnings
    private BigDecimal unpaidSalary;
    private BigDecimal salaryArrears;
    private BigDecimal overtimeDues;
    private BigDecimal leaveEncashment;
    private BigDecimal bonusDues;
    private BigDecimal otherEarnings;
    private BigDecimal totalEarnings;

    // Deductions
    private BigDecimal noticeBuyout;
    private BigDecimal loanRecovery;
    private BigDecimal advanceRecovery;
    private BigDecimal taxDeduction;
    private BigDecimal statutoryDeductions;
    private BigDecimal assetLossRecovery;
    private BigDecimal otherDeductions;
    private BigDecimal totalDeductions;

    // Net Payable
    private BigDecimal netPayable;

    // Status & Approval
    private SettlementStatus settlementStatus;
    private UserDTO hrApprovedBy;
    private LocalDateTime hrApprovedDate;
    private UserDTO financeApprovedBy;
    private LocalDateTime financeApprovedDate;

    // Disbursement
    private LocalDate disbursementDate;
    private PaymentMethod paymentMethod;
    private String paymentReference;
    private String voucherNumber;
    private String statementPath;

    // SLA & Misc
    private LocalDate slaDeadline;
    private Boolean isOverdue;
    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EmployeeSeparationsDTO getSeparation() { return separation; }
    public void setSeparation(EmployeeSeparationsDTO separation) { this.separation = separation; }

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

    public UserDTO getHrApprovedBy() { return hrApprovedBy; }
    public void setHrApprovedBy(UserDTO hrApprovedBy) { this.hrApprovedBy = hrApprovedBy; }

    public LocalDateTime getHrApprovedDate() { return hrApprovedDate; }
    public void setHrApprovedDate(LocalDateTime hrApprovedDate) { this.hrApprovedDate = hrApprovedDate; }

    public UserDTO getFinanceApprovedBy() { return financeApprovedBy; }
    public void setFinanceApprovedBy(UserDTO financeApprovedBy) { this.financeApprovedBy = financeApprovedBy; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
