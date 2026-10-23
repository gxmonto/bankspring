package com.kodigo.bank.service;

import com.kodigo.bank.entity.CreditCard;
import com.kodigo.bank.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;

    public List<CreditCard> getCreditCards() {
        return creditCardRepository.findAll();
    }

    public Optional<CreditCard> getCreditCard(Long id) {
        return creditCardRepository.findById(id);
    }

    public void saveOrUpdate(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }

    public void delete(Long id) {
        creditCardRepository.deleteById(id);
    }
}

