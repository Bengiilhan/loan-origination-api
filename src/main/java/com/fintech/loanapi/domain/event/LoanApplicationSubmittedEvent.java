package com.fintech.loanapi.domain.event;

import java.time.Instant;
import java.util.UUID;

public record LoanApplicationSubmittedEvent(
        UUID loanId,
        UUID customerId,
        Instant occurredOn
) implements DomainEvent {
    public LoanApplicationSubmittedEvent(UUID loanId, UUID customerId) {
        this(loanId, customerId, Instant.now());
    }
}