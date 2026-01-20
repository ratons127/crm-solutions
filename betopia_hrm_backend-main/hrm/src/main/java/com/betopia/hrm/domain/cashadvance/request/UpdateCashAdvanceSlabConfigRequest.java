package com.betopia.hrm.domain.cashadvance.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateCashAdvanceSlabConfigRequest(
        Long employeeTypeId,
        Integer advanceRequestDay,
        Long companyId,
        Long workplaceId,
        Long workplaceGroupId,
        Long businessUnitId,
        LocalDate effectiveFrom,
        LocalDate effectiveTo,
        String remark,
        Boolean isApprovedAmountChange,
        Boolean status,
        BigDecimal advancePercent,
        String serviceChargeType,
        BigDecimal serviceChargeAmount,
        String setupName,
        List<UpdateCashAdvanceSlabConfigDetailRequest> cashAdvanceSlabConfigDetails
) {
}
