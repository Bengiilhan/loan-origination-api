package com.fintech.loanapi.domain.event;

import com.fintech.loanapi.domain.model.Money;
import java.time.Instant;
import java.util.UUID;

public record RepaymentProcessedEvent(
        UUID loanId,
        Money amount,
        Instant occurredOn
) implements DomainEvent {
    public RepaymentProcessedEvent(UUID loanId, Money amount) {
        this(loanId, amount, Instant.now());
    }
}