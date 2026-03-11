package com.fintech.loanapi.application.service;

import com.fintech.loanapi.application.dto.ApplyForLoanCommand;
import com.fintech.loanapi.application.dto.ProcessRepaymentCommand;
import com.fintech.loanapi.application.port.in.LoanUseCase;
import com.fintech.loanapi.application.port.out.EventPublisher;
import com.fintech.loanapi.domain.model.Loan;
import com.fintech.loanapi.domain.model.Money;
import com.fintech.loanapi.domain.repository.LoanRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LoanApplicationService implements LoanUseCase {

    private final LoanRepository loanRepository;
    private final EventPublisher eventPublisher;

    // İleride Message Bus (Kafka) entegrasyonu için EventPublisher portu da buraya eklenecek.

    public LoanApplicationService(LoanRepository loanRepository, EventPublisher eventPublisher) {
        this.loanRepository = loanRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public UUID applyForLoan(ApplyForLoanCommand command) {
        Money principal = new Money(command.principalAmount(), command.currency());
        Loan loan = new Loan(command.customerId(), principal);

        // 1. Veritabanına kaydet
        loanRepository.save(loan);

        // 2. Biriken Event'leri Message Bus'a (Kafka) gönder
        eventPublisher.publish(loan.getDomainEvents());

        // 3. Olaylar fırlatıldıktan sonra listeyi temizle
        loan.clearDomainEvents();

        return loan.getId();
    }

    @Override
    @Transactional
    public void approveLoan(UUID loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Kredi bulunamadı: " + loanId));

        // İş mantığını (Business Logic) Domain nesnesine devrediyoruz
        loan.approve();

        // 1. Veritabanına kaydet (Durum APPROVED olarak güncellenir)
        loanRepository.save(loan);

        // 2. Biriken Event'leri Message Bus'a (Kafka) gönder (Örn: LoanApprovedEvent)
        eventPublisher.publish(loan.getDomainEvents());

        // 3. Olaylar fırlatıldıktan sonra listeyi temizle
        loan.clearDomainEvents();
    }

    @Override
    @Transactional
    public void processRepayment(ProcessRepaymentCommand command) {
        Loan loan = loanRepository.findById(command.loanId())
                .orElseThrow(() -> new IllegalArgumentException("Kredi bulunamadı: " + command.loanId()));

        Money payment = new Money(command.paymentAmount(), command.currency());

        // Geri ödeme iş kuralı ve limit kontrolleri Aggregate içinde yapılır
        loan.makeRepayment(payment);

        // 1. Veritabanına kaydet (Kalan borç güncellenir, gerekirse durum CLOSED olur)
        loanRepository.save(loan);

        // 2. Biriken Event'leri Message Bus'a (Kafka) gönder (Örn: RepaymentProcessedEvent, LoanClosedEvent)
        eventPublisher.publish(loan.getDomainEvents());

        // 3. Olaylar fırlatıldıktan sonra listeyi temizle
        loan.clearDomainEvents();
    }
}