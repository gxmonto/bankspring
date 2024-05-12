package com.kodigo.bank.service;

import com.kodigo.bank.entity.CreditCard;
import com.kodigo.bank.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public List<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    public CreditCard getCreditCardById(Long id) {
        return creditCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Credit card not found"));
    }

    public CreditCard createCreditCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    public CreditCard updateCreditCard(Long id, CreditCard creditCard) {
        CreditCard existingCreditCard = getCreditCardById(id);
        existingCreditCard.setCreditLimit(creditCard.getCreditLimit());
        existingCreditCard.setAnnualFee(creditCard.getAnnualFee());
        existingCreditCard.setCustomer(creditCard.getCustomer());
        existingCreditCard.setBank(creditCard.getBank());
        return creditCardRepository.save(existingCreditCard);
    }

    public void deleteCreditCard(Long id) {
        creditCardRepository.deleteById(id);
    }
}