package com.fintech.loanapi.domain.repository;

import com.fintech.loanapi.domain.model.Loan;
import java.util.Optional;
import java.util.UUID;

public interface LoanRepository {
    void save(Loan loan);
    Optional<Loan> findById(UUID id);
}