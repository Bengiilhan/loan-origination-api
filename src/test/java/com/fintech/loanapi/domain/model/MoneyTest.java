package com.fintech.loanapi.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneySuccessfully() {
        Money money = new Money(new BigDecimal("100.50"), "TRY");
        assertEquals(new BigDecimal("100.50"), money.getAmount());
        assertEquals("TRY", money.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Money(new BigDecimal("-50.00"), "TRY");
        });
        assertEquals("Tutar sıfırdan küçük olamaz.", exception.getMessage());
    }

    @Test
    void shouldAddMoneySuccessfullyWhenCurrenciesMatch() {
        Money m1 = new Money(new BigDecimal("100"), "TRY");
        Money m2 = new Money(new BigDecimal("50"), "TRY");
        Money result = m1.add(m2);

        assertEquals(new BigDecimal("150"), result.getAmount());
        // Value Object'in değişmezliğini (Immutable) test ediyoruz: m1 değişmemiş olmalı
        assertEquals(new BigDecimal("100"), m1.getAmount());
    }

    @Test
    void shouldThrowExceptionWhenSubtractingMoreThanBalance() {
        Money balance = new Money(new BigDecimal("100"), "TRY");
        Money payment = new Money(new BigDecimal("150"), "TRY");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            balance.subtract(payment);
        });
        assertEquals("Yetersiz bakiye.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCurrenciesDoNotMatch() {
        Money tryMoney = new Money(new BigDecimal("100"), "TRY");
        Money usdMoney = new Money(new BigDecimal("50"), "USD");

        assertThrows(IllegalArgumentException.class, () -> {
            tryMoney.add(usdMoney);
        });
    }
}