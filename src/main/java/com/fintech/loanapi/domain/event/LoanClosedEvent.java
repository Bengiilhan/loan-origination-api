package com.fintech.loanapi.domain.event;

import java.time.Instant;
import java.util.UUID;

public record LoanClosedEvent(
        UUID loanId,
        Instant occurredOn
) implements DomainEvent {
    public LoanClosedEvent(UUID loanId) {
        this(loanId, Instant.now());
    }
}