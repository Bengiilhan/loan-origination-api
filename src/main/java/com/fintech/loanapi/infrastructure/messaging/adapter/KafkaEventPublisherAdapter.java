package com.fintech.loanapi.infrastructure.messaging.adapter;

import com.fintech.loanapi.application.port.out.EventPublisher;
import com.fintech.loanapi.domain.event.DomainEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaEventPublisherAdapter implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC_NAME = "loan-events-topic";

    public KafkaEventPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(List<DomainEvent> events) {
        for (DomainEvent event : events) {
            // Olayın sınıf ismini logluyoruz (Örn: LoanApplicationSubmittedEvent)
            System.out.println("KAFKA'YA MESAJ GÖNDERİLİYOR: " + event.getClass().getSimpleName());

            // Event'i Kafka topic'ine fırlatıyoruz
            kafkaTemplate.send(TOPIC_NAME, event.occurredOn().toString(), event);
        }
    }
}