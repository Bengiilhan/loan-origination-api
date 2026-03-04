package com.fintech.loanapi.presentation.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record ApplyForLoanRequest(
        @NotNull(message = "Müşteri ID zorunludur") UUID customerId,
        @NotNull(message = "Tutar zorunludur") @DecimalMin(value = "0.01", message = "Tutar 0'dan büyük olmalıdır") BigDecimal amount,
        @NotBlank(message = "Para birimi zorunludur") String currency
) {}