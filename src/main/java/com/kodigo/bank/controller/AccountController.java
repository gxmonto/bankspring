package com.kodigo.bank.controller;

import com.kodigo.bank.dto.AccountSummary;
import com.kodigo.bank.dto.AmountRequest;
import com.kodigo.bank.dto.TransferRequest;
import com.kodigo.bank.dto.WithdrawalCodeCreated;
import com.kodigo.bank.entity.Transaction;
import com.kodigo.bank.entity.WithdrawalCode;
import com.kodigo.bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

// Banca en linea del cliente que inicio sesion. La cuenta siempre sale del usuario
// autenticado, nunca de un id que mande el navegador.
@RestController
@RequestMapping(path = "api/v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public AccountSummary getSummary(Principal principal) {
        return accountService.getSummary(principal.getName());
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(Principal principal) {
        return accountService.getTransactions(principal.getName());
    }

    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction transfer(@Valid @RequestBody TransferRequest request, Principal principal) {
        return accountService.transfer(principal.getName(), request.toAccountNumber(), request.amount(), request.description());
    }

    @GetMapping("/withdrawal-codes")
    public List<WithdrawalCode> getWithdrawalCodes(Principal principal) {
        return accountService.getWithdrawalCodes(principal.getName());
    }

    @PostMapping("/withdrawal-codes")
    @ResponseStatus(HttpStatus.CREATED)
    public WithdrawalCodeCreated createWithdrawalCode(@Valid @RequestBody AmountRequest request, Principal principal) {
        return accountService.createWithdrawalCode(principal.getName(), request.amount());
    }

    @DeleteMapping("/withdrawal-codes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelWithdrawalCode(@PathVariable Long id, Principal principal) {
        accountService.cancelWithdrawalCode(principal.getName(), id);
    }
}
