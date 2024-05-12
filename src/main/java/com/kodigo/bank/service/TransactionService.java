package com.kodigo.bank.service;

import com.kodigo.bank.repository.TransactionRepository;
import jakarta.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public List<com.kodigo.bank.entity.Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransaction(Long transactionId) {
        return (Transaction) transactionRepository.findById(transactionId).orElseThrow();
    }

    // Additional service methods can be added here
}