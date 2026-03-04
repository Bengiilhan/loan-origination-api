package com.fintech.loanapi.infrastructure.persistence.repository;

import com.fintech.loanapi.infrastructure.persistence.entity.LoanJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//PostgreSQL ile CRUD işlemlerini yapacak olan standart Spring Data arayüzü.
@Repository
public interface SpringDataLoanRepository extends JpaRepository<LoanJpaEntity, UUID> {
}