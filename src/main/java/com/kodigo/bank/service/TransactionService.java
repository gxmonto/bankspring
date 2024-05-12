package com.kodigo.bank.service;

import com.kodigo.bank.entity.Transaction;
import com.kodigo.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }
    public Optional<Transaction> getTransaction(Long id){
        return transactionRepository.findById(id);
    }
    public void saveOrUpdate(Transaction transaction){
        transactionRepository.save(transaction);
    }
    public void delete(Long id){
        transactionRepository.deleteById(id);
    }
}
