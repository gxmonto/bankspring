package com.kodigo.bank.service;

import com.kodigo.bank.entity.Customer;
import com.kodigo.bank.entity.Transaction;
import com.kodigo.bank.entity.TransactionType;
import com.kodigo.bank.exception.InsufficientFundsException;
import com.kodigo.bank.repository.CustomerRepository;
import com.kodigo.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TransactionRepository transactionRepository;


    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public void saveOrUpdateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    // Busca la cuenta de una persona: primero por la llave foranea `id`,
    // y si no existe, por customer_Id = people.id (como estan los datos de db_bank_management.sql)
    public Optional<Customer> findByPeopleId(Long peopleId) {
        return customerRepository.findFirstByPeopleId(peopleId)
                .or(() -> customerRepository.findById(peopleId));
    }

    @Transactional
    public BigDecimal deposit(Long customerId, BigDecimal amount) {
        validateAmount(amount);
        Customer customer = getCustomer(customerId);
        customer.setBalance(currentBalance(customer).add(amount));
        customerRepository.save(customer);
        transactionRepository.save(new Transaction(TransactionType.DEPOSIT, amount, new Date(), customer));
        return customer.getBalance();
    }

    @Transactional
    public BigDecimal withdraw(Long customerId, BigDecimal amount) {
        validateAmount(amount);
        Customer customer = getCustomer(customerId);
        BigDecimal balance = currentBalance(customer);
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds. Current balance: " + balance);
        }
        customer.setBalance(balance.subtract(amount));
        customerRepository.save(customer);
        transactionRepository.save(new Transaction(TransactionType.WITHDRAWAL, amount, new Date(), customer));
        return customer.getBalance();
    }

    public BigDecimal getBalance(Long customerId) {
        return customerRepository.getBalance(customerId);
    }

    public List<Transaction> getTransactions(Long customerId) {
        return transactionRepository.findByCustomerCustomerIdOrderByDateDesc(customerId);
    }

    private Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer " + customerId + " not found"));
    }

    private BigDecimal currentBalance(Customer customer) {
        return customer.getBalance() == null ? BigDecimal.ZERO : customer.getBalance();
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
