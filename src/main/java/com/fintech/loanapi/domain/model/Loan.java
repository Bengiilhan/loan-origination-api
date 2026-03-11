package com.fintech.loanapi.domain.model;

import com.fintech.loanapi.domain.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Loan {
    private final UUID id;
    private final UUID customerId;
    private final Money principal; // Ana para
    private Money remainingBalance; // Kalan borç
    private LoanStatus status;

    // Fırlatılacak Domain Event'leri burada biriktiririz.
    // Tip olarak oluşturduğumuz DomainEvent arayüzünü kullanıyoruz.
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Loan(UUID customerId, Money principal) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.principal = principal;
        this.remainingBalance = principal;
        this.status = LoanStatus.PENDING;

        // 1. Olay: Başvuru alındığında fırlatılır
        this.domainEvents.add(new LoanApplicationSubmittedEvent(this.id, this.customerId));
    }

    public void approve() {
        if (this.status != LoanStatus.PENDING) {
            throw new IllegalStateException("Sadece bekleyen (PENDING) krediler onaylanabilir.");
        }
        this.status = LoanStatus.APPROVED;

        // 2. Olay: Kredi onaylandığında fırlatılır
        this.domainEvents.add(new LoanApprovedEvent(this.id));
    }

    public void reject() {
        if (this.status != LoanStatus.PENDING) {
            throw new IllegalStateException("Sadece bekleyen (PENDING) krediler reddedilebilir.");
        }
        this.status = LoanStatus.REJECTED;
    }

    public void disburse() {
        if (this.status != LoanStatus.APPROVED) {
            throw new IllegalStateException("Sadece onaylanmış (APPROVED) krediler müşteriye aktarılabilir.");
        }
        this.status = LoanStatus.DISBURSED;
    }

    public void makeRepayment(Money paymentAmount) {
        if (this.status != LoanStatus.DISBURSED) {
            throw new IllegalStateException("Sadece aktif (DISBURSED) krediler için ödeme alınabilir.");
        }

        this.remainingBalance = this.remainingBalance.subtract(paymentAmount);

        // 3. Olay: Her ödeme işleminde fırlatılır
        this.domainEvents.add(new RepaymentProcessedEvent(this.id, paymentAmount));

        if (this.remainingBalance.getAmount().doubleValue() == 0) {
            this.status = LoanStatus.CLOSED;

            // 4. Olay: Borç sıfırlandığında fırlatılır
            this.domainEvents.add(new LoanClosedEvent(this.id));
        }
    }
    // Altyapı katmanının veritabanından çektiği verilerle nesneyi yeniden oluşturması için kullanılır
    public static Loan restore(UUID id, UUID customerId, Money principal, Money remainingBalance, LoanStatus status) {
        Loan loan = new Loan(customerId, principal);
        // Reflection kullanmamak için değerleri doğrudan eziyoruz (Event fırlatmaz)
        try {
            java.lang.reflect.Field idField = Loan.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(loan, id);

            loan.remainingBalance = remainingBalance;
            loan.status = status;
            loan.clearDomainEvents(); // İlk yaratılıştaki sahte event'i temizle
        } catch (Exception e) {
            throw new RuntimeException("Kredi nesnesi veritabanından geri yüklenemedi", e);
        }
        return loan;
    }

    public UUID getId() { return id; }
    public UUID getCustomerId() { return customerId; }
    public Money getPrincipal() { return principal; }
    public Money getRemainingBalance() { return remainingBalance; }
    public LoanStatus getStatus() { return status; }

    // Event'leri dışarıya hem salt okunur (read-only) hem de bağımsız bir "kopya" (snapshot) olarak dönüyoruz
    public List<DomainEvent> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    // Olaylar Message Bus'a iletildikten sonra listeyi temizlemek için kullanılır
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
