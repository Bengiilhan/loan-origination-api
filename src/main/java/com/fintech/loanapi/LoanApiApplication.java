package com.fintech.loanapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoanApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApiApplication.class, args);
        System.out.println("========================================================");
        System.out.println("LOAN ORIGINATION API BAŞARIYLA AYAĞA KALKTI");
        System.out.println("========================================================");
    }
}