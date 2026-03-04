package com.fintech.loanapi.infrastructure.persistence.entity;

import com.fintech.loanapi.domain.model.LoanStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

//Bu sınıf tamamen altyapıya aittir. İçinde hiçbir iş kuralı (if/else) barındırmaz, sadece @Entity ve @Table gibi Spring/JPA anotasyonlarını taşır.
@Entity
@Table(name = "loans")
public class LoanJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private BigDecimal principalAmount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal remainingBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status;

    // JPA için boş constructor (Zorunlu)
    protected LoanJpaEntity() {}

    public LoanJpaEntity(UUID id, UUID customerId, BigDecimal principalAmount,
                         String currency, BigDecimal remainingBalance, LoanStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.principalAmount = principalAmount;
        this.currency = currency;
        this.remainingBalance = remainingBalance;
        this.status = status;
    }

    // Getter metodları
    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public String getCurrency() { return currency; }
    public BigDecimal getRemainingBalance() { return remainingBalance; }
    public LoanStatus getStatus() { return status; }
}