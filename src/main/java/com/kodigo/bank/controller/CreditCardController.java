package com.kodigo.bank.controller;

import com.kodigo.bank.entity.CreditCard;
import com.kodigo.bank.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path ="api/v1/creditCard")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;
    @GetMapping
    public List<CreditCard> getAll(){
        return creditCardService.getCreditCards();
    }
    @GetMapping("/{creditCardId}")
    public Optional<CreditCard> getById(@PathVariable("CreditCardId") Long creditCardId){
        return creditCardService.getCreditCard(creditCardId);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUpdate(@RequestBody CreditCard creditCard){
        creditCardService.saveOrUpdate(creditCard);
    }
    @DeleteMapping("/{creditCardId}")
    public void delete(@PathVariable("creditCardId") Long creditCardId){
        creditCardService.delete(creditCardId);
    }

}
