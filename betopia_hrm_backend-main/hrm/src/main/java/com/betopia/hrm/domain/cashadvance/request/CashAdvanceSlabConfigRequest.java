package com.betopia.hrm.domain.cashadvance.request;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfigDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CashAdvanceSlabConfigRequest(
        Integer advanceRequestDay,
        Long employeeTypeId,
        Long companyId,
        Long businessUnitId,
        Long workplaceId,
        Long workplaceGroupId,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        String remark,
        Boolean isApprovedAmountChange,
        Boolean status,
        BigDecimal advancePercent,
        String serviceChargeType,
        BigDecimal serviceChargeAmount,
        String setupName,
        List<CashAdvanceSlabConfigDetailsRequest> cashAdvanceSlabConfigDetails
) {
}
