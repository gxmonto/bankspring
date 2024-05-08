package com.kodigo.bank.service;

import com.kodigo.bank.entity.BankLoan;
import com.kodigo.bank.repository.BankLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankLoanService {
    @Autowired
    BankLoanRepository bankLoanRepository;
    public List<BankLoan> getBankLoans(){
        return bankLoanRepository.findAll();
    }
    public Optional<BankLoan> getBankLoan(Long id){
        return bankLoanRepository.findById(id);
    }
    public void saveOrUpdate(BankLoan bankLoan){
        bankLoanRepository.save(bankLoan);
    }
    public void delete(Long id){
        bankLoanRepository.deleteById(id);
    }
}
