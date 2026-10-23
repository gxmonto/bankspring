package com.kodigo.bank.service;

import com.kodigo.bank.entity.CardPayment;
import com.kodigo.bank.repository.CardPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardPaymentService {
    @Autowired
    private CardPaymentRepository cardPaymentRepository;

    public List<CardPayment> getAllCardPayments() {
        return cardPaymentRepository.findAll();
    }

    public Optional<CardPayment> getCardPaymentById(Long id) {
        return cardPaymentRepository.findById(id);
    }

    public void saveOrUpdateCardPayment(CardPayment cardPayment) {
        cardPaymentRepository.save(cardPayment);
    }

    public void deleteCardPaymentById(Long id) {
        cardPaymentRepository.deleteById(id);
    }
}
