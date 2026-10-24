package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Transaction;
import com.kodigo.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Solo lectura: los movimientos los crean las operaciones (transferencias, cajeros),
// nunca se insertan, editan ni borran a mano.
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
}
