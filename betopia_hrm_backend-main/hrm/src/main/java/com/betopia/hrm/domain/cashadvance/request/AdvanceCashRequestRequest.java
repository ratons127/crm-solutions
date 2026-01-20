package com.betopia.hrm.domain.cashadvance.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.Instant;

public record AdvanceCashRequestRequest(
                                        Instant requestDate,

                                        @NotNull(message = "Amount is required")
                                        @DecimalMin(value = "0.00", message = "Requested amount must be positive")
                                        @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
                                        BigDecimal requestedAmount,

                                        @NotNull(message = "Amount is required")
                                        @DecimalMin(value = "0.00", message = "Requested amount must be positive")
                                        @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
                                        BigDecimal serviceChargeAmount,

                                        @NotNull(message = "Amount is required")
                                        @DecimalMin(value = "0.00", message = "Requested amount must be positive")
                                        @Digits(integer = 16, fraction = 2, message = "Invalid amount format")
                                        BigDecimal deductedAmount,
                                        String reason) {
}
