package com.kodigo.bank.service;

import com.kodigo.bank.dto.AccountHolder;
import com.kodigo.bank.dto.AtmReceipt;
import com.kodigo.bank.entity.*;
import com.kodigo.bank.exception.BusinessException;
import com.kodigo.bank.exception.NotFoundException;
import com.kodigo.bank.repository.CustomerRepository;
import com.kodigo.bank.repository.TransactionRepository;
import com.kodigo.bank.repository.WithdrawalCodeRepository;
import com.kodigo.bank.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Lo que un cajero automatico puede hacer contra el banco: cobrar un codigo de retiro
// sin tarjeta y recibir depositos en efectivo.
@Service
public class AtmService {
    // Mismo mensaje para cualquier fallo, para no revelar si un codigo existe
    static final String INVALID_CODE = "Invalid or expired code, or the amount does not match";

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    WithdrawalCodeRepository withdrawalCodeRepository;
    @Autowired
    AccountLocker accountLocker;

    @Transactional
    public AtmReceipt redeemWithdrawalCode(String atmCode, String code, BigDecimal amount) {
        WithdrawalCode withdrawal = withdrawalCodeRepository.findByCodeHash(Hashing.sha256(code))
                .orElseThrow(() -> new BusinessException(INVALID_CODE));
        if (!"ACTIVE".equals(withdrawal.getDisplayStatus()) || withdrawal.getAmount().compareTo(amount) != 0) {
            throw new BusinessException(INVALID_CODE);
        }

        Customer customer = accountLocker.lock(withdrawal.getCustomer());
        if (customer.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Insufficient funds");
        }
        customer.setBalance(customer.getBalance().subtract(amount));
        withdrawal.setStatus(WithdrawalStatus.REDEEMED);
        withdrawal.setRedeemedAt(LocalDateTime.now());
        withdrawal.setAtmCode(atmCode);
        Transaction transaction = transactionRepository.save(new Transaction(TransactionType.WITHDRAWAL, amount,
                customer.getBalance(), "Cardless withdrawal at ATM " + atmCode, customer));
        return receipt(transaction, "WITHDRAWAL", customer, atmCode);
    }

    @Transactional
    public AtmReceipt deposit(String atmCode, String accountNumber, BigDecimal amount) {
        Customer customer = accountLocker.lock(customerRepository.findByAccountNumber(accountNumber.trim())
                .orElseThrow(() -> new NotFoundException("Account " + accountNumber + " does not exist")));
        customer.setBalance(customer.getBalance().add(amount));
        Transaction transaction = transactionRepository.save(new Transaction(TransactionType.DEPOSIT, amount,
                customer.getBalance(), "Cash deposit at ATM " + atmCode, customer));
        return receipt(transaction, "DEPOSIT", customer, atmCode);
    }

    public AccountHolder lookup(String accountNumber) {
        Customer customer = customerRepository.findByAccountNumber(accountNumber.trim())
                .orElseThrow(() -> new NotFoundException("Account " + accountNumber + " does not exist"));
        return new AccountHolder(customer.getAccountNumber(), maskedName(customer.getPeople()));
    }

    private AtmReceipt receipt(Transaction transaction, String operation, Customer customer, String atmCode) {
        return new AtmReceipt(String.format("TX-%08d", transaction.getTransactionId()), operation, transaction.getAmount(),
                customer.getAccountNumber(), maskedName(customer.getPeople()), atmCode, transaction.getDate());
    }

    // "Gustavo Montoya" -> "Gus**** Mon****"
    static String maskedName(People people) {
        StringBuilder masked = new StringBuilder();
        for (String part : (people.getName() + " " + people.getSurname()).trim().split("\\s+")) {
            if (part.equals("null")) continue;
            int visible = Math.min(3, part.length());
            masked.append(part, 0, visible).append("*".repeat(Math.max(0, part.length() - visible))).append(' ');
        }
        return masked.toString().trim();
    }
}
