package com.fintech.loanapi.domain.model;

public enum LoanStatus {
    PENDING,    // Başvuru yapıldı, onay bekliyor
    APPROVED,   // Onaylandı, dağıtım bekliyor
    REJECTED,   // Reddedildi
    DISBURSED,  // Müşteriye ödendi, taksitler bekleniyor
    CLOSED      // Tüm borç ödendi ve kapandı
}