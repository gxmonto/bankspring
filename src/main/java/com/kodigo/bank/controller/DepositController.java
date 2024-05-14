package com.kodigo.bank.controller;

import com.kodigo.bank.dto.DepositRequest;
import com.kodigo.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
public class DepositController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/api/v1/deposit")
    @ResponseBody
    public BigDecimal deposit(@RequestBody DepositRequest depositRequest) {
        Long customerId = depositRequest.getCustomerId();
        BigDecimal amount = depositRequest.getAmount();

        // Update balance in database
        customerService.deposit(customerId, amount);

        // Return updated balance
        return customerService.getBalance(customerId);
    }
}
