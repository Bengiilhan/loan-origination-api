package com.fintech.loanapi.application.service;

import com.fintech.loanapi.application.dto.ApplyForLoanCommand;
import com.fintech.loanapi.application.port.out.EventPublisher;
import com.fintech.loanapi.domain.event.DomainEvent;
import com.fintech.loanapi.domain.model.Loan;
import com.fintech.loanapi.domain.repository.LoanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// JUnit 5 ile Mockito'yu entegre eden çok önemli anotasyon
@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    // 1. Dış bağımlılıkları (Portları) Mock'luyoruz (Taklit ediyoruz)
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private EventPublisher eventPublisher;

    // 2. Taklit edilen bağımlılıkları asıl test edeceğimiz servise enjekte ediyoruz
    @InjectMocks
    private LoanApplicationService loanApplicationService;

    @Test
    @DisplayName("Geçerli bir başvuru geldiğinde kredi kaydedilmeli ve Event fırlatılmalı")
    void shouldSaveLoanAndPublishEvent_whenValidApplicationSubmitted() {
        // --- ARRANGE (Hazırlık) ---
        UUID customerId = UUID.randomUUID();
        ApplyForLoanCommand command = new ApplyForLoanCommand(
                customerId,
                new BigDecimal("50000.00"),
                "TRY"
        );

        // --- ACT (Eylem) ---
        UUID createdLoanId = loanApplicationService.applyForLoan(command);

        // --- ASSERT (Doğrulama - Mockito'nun gücü burada başlıyor) ---

        // 1. Kredinin bir kimliği (ID) oluştuğunu doğrula
        assertNotNull(createdLoanId);

        // 2. Repository'nin save() metodunun HERHANGİ BİR Loan nesnesi ile TAM OLARAK 1 KERE çağrıldığını doğrula
        verify(loanRepository, times(1)).save(any(Loan.class));

        // 3. Olayları (Events) yakalamak için bir "Kapan" (Captor) oluşturuyoruz
        ArgumentCaptor<List<DomainEvent>> eventCaptor = ArgumentCaptor.forClass(List.class);

        // EventPublisher'ın publish metodunun çağrıldığını onayla ve fırlatılan parametreyi yakala
        verify(eventPublisher, times(1)).publish(eventCaptor.capture());

        // Yakalanan olay listesini al
        List<DomainEvent> publishedEvents = eventCaptor.getValue();

        // 4. Doğrulamalar: Listede tam olarak 1 olay olmalı ve bu LoanApplicationSubmittedEvent olmalı
        assertEquals(1, publishedEvents.size());
        assertEquals("LoanApplicationSubmittedEvent", publishedEvents.get(0).getClass().getSimpleName());
    }
}