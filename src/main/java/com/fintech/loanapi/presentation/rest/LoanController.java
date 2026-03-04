package com.fintech.loanapi.presentation.rest;

import com.fintech.loanapi.application.dto.ApplyForLoanCommand;
import com.fintech.loanapi.application.dto.ProcessRepaymentCommand;
import com.fintech.loanapi.application.port.in.LoanUseCase;
import com.fintech.loanapi.presentation.request.ApplyForLoanRequest;
import com.fintech.loanapi.presentation.request.ProcessRepaymentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


//Bu sınıf doğrudan LoanApplicationService'e değil, onun arayüzü olan LoanUseCase (Inbound Port) portuna bağımlı
@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanUseCase loanUseCase;

    public LoanController(LoanUseCase loanUseCase) {
        this.loanUseCase = loanUseCase;
    }

    // 1. Kredi Başvurusu Yap (POST /api/v1/loans)
    @PostMapping
    public ResponseEntity<UUID> applyForLoan(@RequestBody @Valid ApplyForLoanRequest request) {
        // Request -> Command Mapping
        ApplyForLoanCommand command = new ApplyForLoanCommand(
                request.customerId(),
                request.amount(),
                request.currency()
        );

        UUID loanId = loanUseCase.applyForLoan(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(loanId);
    }

    // 2. Krediyi Onayla (PATCH /api/v1/loans/{id}/approve)
    @PatchMapping("/{id}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approveLoan(@PathVariable UUID id) {
        loanUseCase.approveLoan(id);
    }

    // 3. Geri Ödeme Yap (POST /api/v1/loans/repayments)
    @PostMapping("/repayments")
    public ResponseEntity<Void> processRepayment(@RequestBody @Valid ProcessRepaymentRequest request) {
        // Request -> Command Mapping
        ProcessRepaymentCommand command = new ProcessRepaymentCommand(
                request.loanId(),
                request.amount(),
                request.currency()
        );

        loanUseCase.processRepayment(command);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}