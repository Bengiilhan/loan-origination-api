package com.fintech.loanapi.infrastructure.persistence.adapter;

import com.fintech.loanapi.domain.model.Loan;
import com.fintech.loanapi.domain.model.Money;
import com.fintech.loanapi.domain.repository.LoanRepository;
import com.fintech.loanapi.infrastructure.persistence.entity.LoanJpaEntity;
import com.fintech.loanapi.infrastructure.persistence.repository.SpringDataLoanRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class LoanRepositoryAdapter implements LoanRepository {

    private final SpringDataLoanRepository springDataRepository;

    public LoanRepositoryAdapter(SpringDataLoanRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public void save(Loan loan) {
        // Domain nesnesini -> JPA nesnesine çevir (Mapper)
        LoanJpaEntity entity = new LoanJpaEntity(
                loan.getId(),
                loan.getCustomerId(),
                loan.getPrincipal().getAmount(),
                loan.getPrincipal().getCurrency(),
                loan.getRemainingBalance().getAmount(),
                loan.getStatus()
        );

        springDataRepository.save(entity);
    }

    @Override
    public Optional<Loan> findById(UUID id) {
        // JPA nesnesini -> Domain nesnesine çevir (Mapper)
        return springDataRepository.findById(id).map(entity -> {
            Money principal = new Money(entity.getPrincipalAmount(), entity.getCurrency());
            Money remaining = new Money(entity.getRemainingBalance(), entity.getCurrency());

            // Veritabanından gelen verilerle saf Domain nesnesini yeniden ayağa kaldırıyoruz
            return Loan.restore(
                    entity.getId(),
                    entity.getCustomerId(),
                    principal,
                    remaining,
                    entity.getStatus()
            );
        });
    }
}