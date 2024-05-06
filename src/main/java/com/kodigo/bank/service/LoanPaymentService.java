package com.kodigo.bank.service;

import com.kodigo.bank.entity.LoanPayment;
import com.kodigo.bank.repository.LoanPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanPaymentService {
    @Autowired
    LoanPaymentRepository loanPaymentRepository;
    public List<LoanPayment> getLoanPayments(){
        return loanPaymentRepository.findAll();
    }
    public Optional<LoanPayment> getLoanPayment(Long id){
        return loanPaymentRepository.findById(id);
    }
    public void saveOrUpdate(LoanPayment loanPayment){
        loanPaymentRepository.save(loanPayment);
    }
    public void delete(Long id){
        loanPaymentRepository.deleteById(id);
    }
}
