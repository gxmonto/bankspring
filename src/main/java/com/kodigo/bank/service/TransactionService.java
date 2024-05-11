package com.kodigo.bank.service;

import com.kodigo.bank.repository.TransactionRepository;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<com.kodigo.bank.entity.Transaction> getAllTransactions() {
        List<com.kodigo.bank.entity.Transaction> all = transactionRepository.findAll();
        return all;
    }

    @Transactional(readOnly = true)
    public com.kodigo.bank.entity.Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow();
    }

    @Transactional
    public S updateTransaction(Transaction transaction) {
        S save = transactionRepository.save(transaction);
        return save;
    }

    @Transactional
    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findByCustomer_CustomerId(customerId);
    }
}
