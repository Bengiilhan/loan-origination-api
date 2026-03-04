package com.fintech.loanapi.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}