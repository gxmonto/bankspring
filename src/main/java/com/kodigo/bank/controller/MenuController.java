package com.kodigo.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/")
    public String login() {
        return "login";
    }
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/bank-loan")
    public String bankLoan() {
        return "BankLoan";
    }

    @GetMapping("/loan-payment")
    public String loanPayment() {
        return "LoanPayment";
    }

    @GetMapping("/loan-management")
    public String loanManagement() {
        return "loan-management";
    }

    @GetMapping("/card-management")
    public String cardManagement() {
        return "card-management";
    }

    @GetMapping("/card-payment")
    public String cardPayment() {
        return "CardPayment";
    }

    @GetMapping("/credit-card")
    public String creditCard() {
        return "CreditCard";
    }

    @GetMapping("/withdraw")
    public String withdraw() {
        return "Withdraw";
    }

    @GetMapping("/deposit")
    public String deposit() {
        return "Deposit";
    }
}
