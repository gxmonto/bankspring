package com.kodigo.bank.controller;

import com.kodigo.bank.entity.LoanPayment;
import com.kodigo.bank.service.LoanPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/loanPayment")
public class LoanPaymentController {
    @Autowired
    private LoanPaymentService loanPaymentService;
    @GetMapping
    public List<LoanPayment> getAll(){
        return loanPaymentService.getLoanPayments();
    }
    @GetMapping("/{paymentID}")
    public Optional<LoanPayment> getById(@PathVariable("paymentID") Long paymentID){
        return loanPaymentService.getLoanPayment(paymentID);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUpdate(@RequestBody LoanPayment loanPayment){
        loanPaymentService.saveOrUpdate(loanPayment);
    }
    @DeleteMapping("/{paymentID}")
    public void delete(@PathVariable("paymentID") Long paymentId){
        loanPaymentService.delete(paymentId);
    }

}
