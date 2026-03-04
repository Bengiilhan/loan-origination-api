package com.fintech.loanapi.application.port.in;

import com.fintech.loanapi.application.dto.ApplyForLoanCommand;
import com.fintech.loanapi.application.dto.ProcessRepaymentCommand;
import java.util.UUID;

//Dış dünyanın (Controller'ların) sistemimize hangi işlemleri yaptırabileceğini belirten arayüzdür.
public interface LoanUseCase {
    UUID applyForLoan(ApplyForLoanCommand command);
    void approveLoan(UUID loanId);
    void processRepayment(ProcessRepaymentCommand command);
}