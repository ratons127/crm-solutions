package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.PaymentMethod;
import com.betopia.hrm.domain.employee.enums.SettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FinalSettlementRequest(

        Long separationId,
        BigDecimal unpaidSalary,
        BigDecimal salaryArrears,
        BigDecimal overtimeDues,
        BigDecimal leaveEncashment,
        BigDecimal bonusDues,
        BigDecimal otherEarnings,
        BigDecimal totalEarnings,

        BigDecimal noticeBuyout,
        BigDecimal loanRecovery,
        BigDecimal advanceRecovery,
        BigDecimal taxDeduction,
        BigDecimal statutoryDeductions,
        BigDecimal assetLossRecovery,
        BigDecimal otherDeductions,
        BigDecimal totalDeductions,

        BigDecimal netPayable,

        SettlementStatus settlementStatus,
        Long hrApprovedById,
        LocalDateTime hrApprovedDate,
        Long financeApprovedById,
        LocalDateTime financeApprovedDate,

        LocalDate disbursementDate,
        PaymentMethod paymentMethod,
        String paymentReference,
        String voucherNumber,
        String statementPath,
        LocalDate slaDeadline,
        Boolean isOverdue,
        String remarks
) {
}
