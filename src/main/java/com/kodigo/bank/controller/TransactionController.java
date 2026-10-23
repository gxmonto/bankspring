package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Transaction;
import com.kodigo.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path ="api/v1/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll() {
        return transactionService.getTransactions();
    }

    @GetMapping("/{transactionId}")
    public Optional<Transaction> getById(@PathVariable("transactionId") Long transactionId) {
        return transactionService.getTransaction(transactionId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUpdate(@RequestBody Transaction transaction) {
        transactionService.saveOrUpdate(transaction);
    }

    @DeleteMapping("/{transactionId}")
    public void delete(@PathVariable("transactionId") Long transactionId) {
        transactionService.delete(transactionId);
    }
}
