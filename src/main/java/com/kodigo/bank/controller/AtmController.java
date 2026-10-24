package com.kodigo.bank.controller;

import com.kodigo.bank.dto.AccountHolder;
import com.kodigo.bank.dto.AtmDepositRequest;
import com.kodigo.bank.dto.AtmReceipt;
import com.kodigo.bank.dto.AtmWithdrawalRequest;
import com.kodigo.bank.service.AtmService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// API que usan los cajeros automaticos. Cada cajero se autentica con los headers
// X-ATM-ID y X-ATM-KEY (ver AtmAuthenticationFilter); aqui el Principal es el cajero.
@RestController
@RequestMapping(path = "api/atm/v1")
public class AtmController {
    @Autowired
    private AtmService atmService;

    @GetMapping("/accounts/{accountNumber}")
    public AccountHolder lookup(@PathVariable String accountNumber) {
        return atmService.lookup(accountNumber);
    }

    @PostMapping("/withdrawals")
    public AtmReceipt withdraw(@Valid @RequestBody AtmWithdrawalRequest request, Principal atm) {
        return atmService.redeemWithdrawalCode(atm.getName(), request.code(), request.amount());
    }

    @PostMapping("/deposits")
    public AtmReceipt deposit(@Valid @RequestBody AtmDepositRequest request, Principal atm) {
        return atmService.deposit(atm.getName(), request.accountNumber(), request.amount());
    }
}
