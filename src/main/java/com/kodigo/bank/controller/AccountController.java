package com.kodigo.bank.controller;

import com.kodigo.bank.dto.AmountRequest;
import com.kodigo.bank.entity.Transaction;
import com.kodigo.bank.exception.InsufficientFundsException;
import com.kodigo.bank.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

// Operaciones de la cuenta del usuario que inicio sesion.
// El customerId se toma de la sesion (cookie JSESSIONID), no del navegador,
// asi nadie puede depositar o retirar de una cuenta ajena cambiando la URL.
@RestController
@RequestMapping(path = "api/v1/account")
public class AccountController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/balance")
    public Map<String, BigDecimal> getBalance(HttpSession session) {
        return Map.of("balance", customerService.getBalance(currentCustomerId(session)));
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(HttpSession session) {
        return customerService.getTransactions(currentCustomerId(session));
    }

    @PostMapping("/deposit")
    public Map<String, BigDecimal> deposit(@RequestBody AmountRequest request, HttpSession session) {
        return Map.of("balance", customerService.deposit(currentCustomerId(session), request.getAmount()));
    }

    @PostMapping("/withdraw")
    public Map<String, BigDecimal> withdraw(@RequestBody AmountRequest request, HttpSession session) {
        return Map.of("balance", customerService.withdraw(currentCustomerId(session), request.getAmount()));
    }

    private Long currentCustomerId(HttpSession session) {
        Object customerId = session.getAttribute(LoginController.CUSTOMER_ID);
        if (customerId == null) {
            throw new NotLoggedInException();
        }
        return (Long) customerId;
    }

    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity<Map<String, String>> handleNotLoggedIn() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Please log in"));
    }

    @ExceptionHandler({IllegalArgumentException.class, InsufficientFundsException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    static class NotLoggedInException extends RuntimeException {
    }
}
