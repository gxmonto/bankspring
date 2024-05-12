package com.kodigo.bank.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/credit-cards")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping
    public List<CreditCard> getAllCreditCards() {
        return creditCardService.getAllCreditCards();
    }

    @GetMapping("/{id}")
    public CreditCard getCreditCardById(@PathVariable Long id) {
        return creditCardService.getCreditCardById(id);
    }

    @PostMapping
    public CreditCard createCreditCard(@RequestBody CreditCard creditCard) {
        return creditCardService.createCreditCard(creditCard);
    }

    @PutMapping("/{id}")
    public CreditCard updateCreditCard(@PathVariable Long id, @RequestBody CreditCard creditCard) {
        return creditCardService.updateCreditCard(id, creditCard);
    }

    @DeleteMapping("/{id}")
    public void deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
    }
}