package com.kodigo.bank.controller;

import com.kodigo.bank.entity.CardPayment;
import com.kodigo.bank.service.CardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path ="api/v1/cardpayment")
public class CardPaymentController {
    @Autowired
    private CardPaymentService cardPaymentService;

    @GetMapping
    public List<CardPayment> getAll() {
        return cardPaymentService.getAllCardPayments();
    }

    @GetMapping("/{cardPaymentId}")
    public Optional<CardPayment> getById(@PathVariable("cardPaymentId") Long cardPaymentId) {
        return cardPaymentService.getCardPaymentById(cardPaymentId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveUpdate(@RequestBody CardPayment cardPayment) {
        cardPaymentService.saveOrUpdateCardPayment(cardPayment);
    }

    @DeleteMapping("/{cardPaymentId}")
    public void delete(@PathVariable("cardPaymentId") Long cardPaymentId) {
        cardPaymentService.deleteCardPaymentById(cardPaymentId);
    }
}
