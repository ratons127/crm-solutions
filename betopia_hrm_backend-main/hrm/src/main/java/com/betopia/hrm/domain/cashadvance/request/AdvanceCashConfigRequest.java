package com.betopia.hrm.domain.cashadvance.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AdvanceCashConfigRequest(

                                       @NotNull(message = "Quantity is required")
                                       @Min(value = 0, message = "Quantity must be >= 0")
                                       Integer minimumWorkingDays,

                                       Long employeeTypeId,

                                       @NotNull(message = "Percent is required")
                                       @DecimalMin(value = "0.00", message = "Percent must be positive")
                                       @DecimalMax(value = "100.00", message = "Percent cannot exceed 100")
                                       BigDecimal advanceLimitPercent,

                                       @NotNull(message = "Amount is required")
                                       @DecimalMin(value = "0.00", message = "Requested amount must be positive")
                                       @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
                                       BigDecimal advanceLimitAmount,

                                       @NotNull(message = "Percent is required")
                                       @DecimalMin(value = "0.00", message = "Percent must be positive")
                                       @DecimalMax(value = "100.00", message = "Percent cannot be exceed 100")
                                       BigDecimal serviceChargePercent,

                                       @NotNull(message = "Amount is required")
                                       @DecimalMin(value = "0.00", message = "Requested amount must be positive")
                                       @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
                                       BigDecimal serviceChargeAmount,

                                       Boolean isApprovedAmountChange,
                                       Boolean status) {
}
