package com.fintech.loanapi.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ApplyForLoanCommand(
        UUID customerId,
        BigDecimal principalAmount,
        String currency
) {}