package com.fintech.loanapi.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    private UUID customerId;
    private Money principal;
    private Loan loan;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        principal = new Money(new BigDecimal("10000.00"), "TRY");
        loan = new Loan(customerId, principal);
    }

    @Test
    void shouldInitializeLoanInPendingState() {
        assertNotNull(loan.getId());
        assertEquals(customerId, loan.getCustomerId());
        assertEquals(principal, loan.getPrincipal());
        assertEquals(principal, loan.getRemainingBalance());
        assertEquals(LoanStatus.PENDING, loan.getStatus());
    }

    @Test
    void shouldApprovePendingLoan() {
        loan.approve();
        assertEquals(LoanStatus.APPROVED, loan.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenApprovingAlreadyApprovedLoan() {
        loan.approve(); // İlk onay başarılı

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            loan.approve(); // İkinci deneme hata fırlatmalı
        });
        assertEquals("Sadece bekleyen (PENDING) krediler onaylanabilir.", exception.getMessage());
    }

    @Test
    void shouldDisburseApprovedLoan() {
        loan.approve();
        loan.disburse();
        assertEquals(LoanStatus.DISBURSED, loan.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDisbursingPendingLoan() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            loan.disburse(); // Onaylanmadan dağıtıma çıkılamaz
        });
        assertEquals("Sadece onaylanmış (APPROVED) krediler müşteriye aktarılabilir.", exception.getMessage());
    }

    @Test
    void shouldProcessRepaymentAndReduceBalance() {
        loan.approve();
        loan.disburse(); // Kredi aktif (DISBURSED) duruma geldi

        Money payment = new Money(new BigDecimal("3000.00"), "TRY");
        loan.makeRepayment(payment);

        assertEquals(new BigDecimal("7000.00"), loan.getRemainingBalance().getAmount());
        assertEquals(LoanStatus.DISBURSED, loan.getStatus()); // Borç bitmediği için hala aktif
    }

    @Test
    void shouldCloseLoanWhenBalanceReachesZero() {
        loan.approve();
        loan.disburse();

        Money fullPayment = new Money(new BigDecimal("10000.00"), "TRY");
        loan.makeRepayment(fullPayment);

        assertEquals(new BigDecimal("0.00"), loan.getRemainingBalance().getAmount());
        assertEquals(LoanStatus.CLOSED, loan.getStatus()); // Borç bitti, kredi kapandı
    }

    @Test
    void shouldThrowExceptionWhenRepaymentMadeOnPendingLoan() {
        Money payment = new Money(new BigDecimal("1000.00"), "TRY");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            loan.makeRepayment(payment); // Kredi henüz müşteriye verilmeden ödeme alınamaz
        });
        assertEquals("Sadece aktif (DISBURSED) krediler için ödeme alınabilir.", exception.getMessage());
    }
}