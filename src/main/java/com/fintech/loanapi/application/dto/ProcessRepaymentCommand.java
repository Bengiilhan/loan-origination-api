package com.fintech.loanapi.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessRepaymentCommand(
        UUID loanId,
        BigDecimal paymentAmount,
        String currency
) {}