package com.kodigo.bank.service;

import com.kodigo.bank.dto.AccountSummary;
import com.kodigo.bank.dto.WithdrawalCodeCreated;
import com.kodigo.bank.entity.*;
import com.kodigo.bank.exception.BusinessException;
import com.kodigo.bank.exception.NotFoundException;
import com.kodigo.bank.repository.CustomerRepository;
import com.kodigo.bank.repository.TransactionRepository;
import com.kodigo.bank.repository.WithdrawalCodeRepository;
import com.kodigo.bank.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

// Operaciones de la banca en linea del cliente: consultar, transferir y generar codigos de retiro.
// El dinero en efectivo solo entra o sale por un cajero (ver AtmService).
@Service
public class AccountService {
    static final BigDecimal MAX_WITHDRAWAL = new BigDecimal("1000");
    static final BigDecimal BILL = new BigDecimal("10");

    private final SecureRandom random = new SecureRandom();

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    WithdrawalCodeRepository withdrawalCodeRepository;
    @Autowired
    AccountLocker accountLocker;

    @Value("${bank.withdrawal-code.ttl-minutes:30}")
    long codeTtlMinutes;

    public Customer getCustomer(String username) {
        return customerRepository.findFirstByPeopleUsername(username)
                .orElseThrow(() -> new NotFoundException("No bank account for user " + username));
    }

    public AccountSummary getSummary(String username) {
        Customer customer = getCustomer(username);
        BigDecimal reserved = withdrawalCodeRepository.sumReserved(customer.getCustomerId(), LocalDateTime.now());
        People people = customer.getPeople();
        return new AccountSummary(customer.getAccountNumber(), people.getName() + " " + people.getSurname(),
                customer.getBalance(), reserved, customer.getBalance().subtract(reserved));
    }

    public List<Transaction> getTransactions(String username) {
        return transactionRepository.findTop50ByCustomerCustomerIdOrderByDateDescTransactionIdDesc(getCustomer(username).getCustomerId());
    }

    @Transactional
    public Transaction transfer(String username, String toAccountNumber, BigDecimal amount, String description) {
        Customer from = getCustomer(username);
        Customer to = customerRepository.findByAccountNumber(toAccountNumber.trim())
                .orElseThrow(() -> new NotFoundException("Account " + toAccountNumber + " does not exist"));
        if (from.getCustomerId().equals(to.getCustomerId())) {
            throw new BusinessException("You cannot transfer to your own account");
        }

        // Siempre se bloquea primero la cuenta con id menor, para evitar deadlocks
        // si A transfiere a B mientras B transfiere a A
        boolean fromFirst = from.getCustomerId() < to.getCustomerId();
        accountLocker.lock(fromFirst ? from : to);
        accountLocker.lock(fromFirst ? to : from);

        requireAvailable(from, amount);
        String note = description == null || description.isBlank() ? "" : " - " + description.trim();

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        transactionRepository.save(new Transaction(TransactionType.TRANSFER_IN, amount, to.getBalance(),
                "Transfer from " + from.getAccountNumber() + note, to));
        return transactionRepository.save(new Transaction(TransactionType.TRANSFER_OUT, amount, from.getBalance(),
                "Transfer to " + to.getAccountNumber() + note, from));
    }

    @Transactional
    public WithdrawalCodeCreated createWithdrawalCode(String username, BigDecimal amount) {
        if (amount.compareTo(MAX_WITHDRAWAL) > 0) {
            throw new BusinessException("The maximum cardless withdrawal is $" + MAX_WITHDRAWAL);
        }
        if (amount.remainder(BILL).compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("The amount must be a multiple of $" + BILL + " (ATMs only dispense bills)");
        }
        Customer customer = accountLocker.lock(getCustomer(username));
        requireAvailable(customer, amount);

        String code;
        do {
            code = String.format("%08d", random.nextInt(100_000_000));
        } while (withdrawalCodeRepository.existsByCodeHash(Hashing.sha256(code)));

        WithdrawalCode withdrawal = new WithdrawalCode();
        withdrawal.setCodeHash(Hashing.sha256(code));
        withdrawal.setCodeHint(code.substring(6));
        withdrawal.setAmount(amount);
        withdrawal.setStatus(WithdrawalStatus.ACTIVE);
        withdrawal.setCreatedAt(LocalDateTime.now());
        withdrawal.setExpiresAt(withdrawal.getCreatedAt().plusMinutes(codeTtlMinutes));
        withdrawal.setCustomer(customer);
        withdrawalCodeRepository.save(withdrawal);
        return new WithdrawalCodeCreated(withdrawal.getId(), code, amount, withdrawal.getExpiresAt());
    }

    public List<WithdrawalCode> getWithdrawalCodes(String username) {
        return withdrawalCodeRepository.findTop20ByCustomerCustomerIdOrderByCreatedAtDesc(getCustomer(username).getCustomerId());
    }

    @Transactional
    public void cancelWithdrawalCode(String username, Long id) {
        WithdrawalCode withdrawal = withdrawalCodeRepository.findByIdAndCustomerCustomerId(id, getCustomer(username).getCustomerId())
                .orElseThrow(() -> new NotFoundException("Withdrawal code not found"));
        if (!"ACTIVE".equals(withdrawal.getDisplayStatus())) {
            throw new BusinessException("Only active codes can be cancelled");
        }
        withdrawal.setStatus(WithdrawalStatus.CANCELLED);
    }

    void requireAvailable(Customer customer, BigDecimal amount) {
        BigDecimal available = customer.getBalance()
                .subtract(withdrawalCodeRepository.sumReserved(customer.getCustomerId(), LocalDateTime.now()));
        if (available.compareTo(amount) < 0) {
            throw new BusinessException("Insufficient funds. Available balance: $" + available);
        }
    }
}
