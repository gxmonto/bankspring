package com.kodigo.bank.controller;

import com.kodigo.bank.entity.BankLoan;
import com.kodigo.bank.service.BankLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path ="api/v1/bankLoan")
public class BankLoanController {
    @Autowired
    private BankLoanService bankLoanService;
    @GetMapping
    public List<BankLoan> getAll(){
        return bankLoanService.getBankLoans();
    }
    @GetMapping("/{loanId}")
    public Optional<BankLoan> getById(@PathVariable("loanId") Long loanId){
        return bankLoanService.getBankLoan(loanId);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUpdate(@RequestBody BankLoan bankLoan){
        bankLoanService.saveOrUpdate(bankLoan);
    }
    @DeleteMapping("/{loanId}")
    public void delete(@PathVariable("loanId") Long loanId){
        bankLoanService.delete(loanId);
    }

}
