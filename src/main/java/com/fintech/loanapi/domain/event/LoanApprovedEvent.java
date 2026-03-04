package com.fintech.loanapi.domain.event;

import java.time.Instant;
import java.util.UUID;

public record LoanApprovedEvent(
        UUID loanId,
        Instant occurredOn
) implements DomainEvent {
    public LoanApprovedEvent(UUID loanId) {
        this(loanId, Instant.now());
    }
}