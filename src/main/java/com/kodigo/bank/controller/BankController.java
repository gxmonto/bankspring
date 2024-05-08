package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Bank;
import com.kodigo.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/banks")
public class BankController {
    @Autowired
    private BankService bankService;

    @GetMapping
    public List<Bank> getAll() {
        return bankService.getBanks();
    }

    @GetMapping("/{id}")
    public Optional<Bank> getById(@PathVariable("id") Long id) {
        return bankService.getBankById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveOrUpdate(@RequestBody Bank bank) {
        bankService.saveOrUpdateBank(bank);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        bankService.deleteBank(id);
    }
}
