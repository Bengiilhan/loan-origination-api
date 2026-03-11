package com.fintech.loanapi.application.port.out;

import com.fintech.loanapi.domain.event.DomainEvent;
import java.util.List;

public interface EventPublisher {
    void publish(List<DomainEvent> events);
}