package com.fintech.loanapi.presentation.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record ProcessRepaymentRequest(
        @NotNull(message = "Kredi ID zorunludur") UUID loanId,
        @NotNull(message = "Ödeme tutarı zorunludur") @DecimalMin(value = "0.01", message = "Ödeme tutarı 0'dan büyük olmalıdır") BigDecimal amount,
        @NotNull(message = "Para birimi zorunludur") String currency
) {}