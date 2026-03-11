package com.fintech.loanapi.infrastructure.messaging.adapter;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LoanEventListener {

    // "loan-events-topic" kuyruğunu dinliyoruz.
    // Gelen JSON verisini şimdilik düz metin (String) olarak yakalayıp ekrana basacağız.
    @KafkaListener(topics = "loan-events-topic", groupId = "notification-service-group")
    public void handleLoanEvent(String eventPayload) {
        System.out.println("\n========================================================");
        System.out.println("📬 [BİLDİRİM SERVİSİ] YENİ BİR OLAY YAKALANDI!");
        System.out.println("Sisteme bir mesaj düştü. İçerik (JSON):");
        System.out.println(eventPayload);
        System.out.println("========================================================\n");
    }
}
