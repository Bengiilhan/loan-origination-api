package com.fintech.loanapi.application.service;

import com.fintech.loanapi.application.dto.ApplyForLoanCommand;
import com.fintech.loanapi.application.dto.ProcessRepaymentCommand;
import com.fintech.loanapi.application.port.in.LoanUseCase;
import com.fintech.loanapi.domain.model.Loan;
import com.fintech.loanapi.domain.model.Money;
import com.fintech.loanapi.domain.repository.LoanRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LoanApplicationService implements LoanUseCase {

    private final LoanRepository loanRepository;

    // İleride Message Bus (Kafka) entegrasyonu için EventPublisher portu da buraya eklenecek.

    public LoanApplicationService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    @Transactional // İşlemsel Tutarlılık (Unit of Work)
    public UUID applyForLoan(ApplyForLoanCommand command) {
        Money principal = new Money(command.principalAmount(), command.currency());
        Loan loan = new Loan(command.customerId(), principal);

        loanRepository.save(loan);

        // TODO: Event'ler burada Message Bus'a gönderilecek
        // eventPublisher.publish(loan.getDomainEvents());
        // loan.clearDomainEvents();

        return loan.getId();
    }

    @Override
    @Transactional
    public void approveLoan(UUID loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Kredi bulunamadı: " + loanId));

        // İş mantığını (Business Logic) Domain nesnesine devrediyoruz
        loan.approve();

        loanRepository.save(loan);
    }

    @Override
    @Transactional
    public void processRepayment(ProcessRepaymentCommand command) {
        Loan loan = loanRepository.findById(command.loanId())
                .orElseThrow(() -> new IllegalArgumentException("Kredi bulunamadı: " + command.loanId()));

        Money payment = new Money(command.paymentAmount(), command.currency());

        // Geri ödeme iş kuralı ve limit kontrolleri Aggregate içinde yapılır
        loan.makeRepayment(payment);

        loanRepository.save(loan);
    }
}