package com.kodigo.bank.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));
        return isAdmin && !isCustomer ? "redirect:/admin" : "index";
    }

    @GetMapping("/transfer")
    public String transfer() {
        return "transfer";
    }

    @GetMapping("/withdraw")
    public String withdraw() {
        return "withdraw";
    }

    @GetMapping("/deposit")
    public String deposit() {
        return "deposit";
    }

    // Simulador de cajero automatico: una pagina independiente que habla con la API de cajeros
    @GetMapping("/atm")
    public String atm() {
        return "atm";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    // Paginas de administracion de prestamos y tarjetas (2024, pendientes de terminar)
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
}
