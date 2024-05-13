package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Customer;
import com.kodigo.bank.entity.Transaction;
import com.kodigo.bank.entity.TransactionType;
import com.kodigo.bank.service.CustomerService;
import com.kodigo.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

// DepositWithdrawalController.java
@RestController
@RequestMapping(path = "api/v1/deposit-withdrawal")
public class DepositWithdrawalController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long customerId, @RequestParam BigDecimal amount) {
        Customer customer = customerService.getCustomerById(customerId).orElseThrow();
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, new Date(), customer);
        transactionService.saveOrUpdate(transaction);
        customer.setBalance(customer.getBalance().add(amount));
        customerService.saveOrUpdateCustomer(customer);
        return ResponseEntity.ok("Deposit successful. New balance: " + customer.getBalance());
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(@RequestParam Long customerId, @RequestParam BigDecimal amount) {
        Customer customer = customerService.getCustomerById(customerId).orElseThrow();
        if (customer.getBalance().compareTo(amount) < 0) {
            return ResponseEntity.badRequest().body("Insufficient balance");
        }
        Transaction transaction = new Transaction(TransactionType.WITHDRAWAL, amount, new Date(), customer);
        transactionService.saveOrUpdate(transaction);
        customer.setBalance(customer.getBalance().subtract(amount));
        customerService.saveOrUpdateCustomer(customer);
        return ResponseEntity.ok("Withdrawal successful. New balance: " + customer.getBalance());
    }
}