package com.kodigo.bank.service;

import com.kodigo.bank.entity.Customer;
import com.kodigo.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final SecureRandom random = new SecureRandom();

    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // El saldo nunca se cambia desde aqui: una cuenta nueva abre en 0 y una existente
    // conserva su saldo. El dinero solo se mueve con depositos, retiros y transferencias,
    // asi cada cambio queda en el historial de movimientos.
    public void saveOrUpdateCustomer(Customer customer) {
        Optional<Customer> existing = customer.getCustomerId() == null
                ? Optional.empty() : customerRepository.findById(customer.getCustomerId());
        customer.setBalance(existing.map(Customer::getBalance).orElse(BigDecimal.ZERO));
        if (customer.getAccountNumber() == null || customer.getAccountNumber().isBlank()) {
            customer.setAccountNumber(existing.map(Customer::getAccountNumber).orElseGet(this::newAccountNumber));
        }
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public BigDecimal getBalance(Long customerId) {
        return customerRepository.getBalance(customerId);
    }

    private String newAccountNumber() {
        String number;
        do {
            number = String.format("%04d-%04d", 1000 + random.nextInt(9000), random.nextInt(10000));
        } while (customerRepository.existsByAccountNumber(number));
        return number;
    }
}
