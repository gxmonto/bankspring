package com.kodigo.bank.service;

import com.kodigo.bank.entity.Customer;
import com.kodigo.bank.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;


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

    public void deposit(Long customerId, BigDecimal amount) {
        Customer customer = customerRepository.getById(customerId);
        BigDecimal currentBalance = customer.getBalance();
        BigDecimal newBalance = currentBalance.add(amount);
        customer.setBalance(newBalance);
        customerRepository.save(customer);
    }

    public BigDecimal getBalance(Long customerId) {
        return customerRepository.getBalance(customerId);
    }
}
