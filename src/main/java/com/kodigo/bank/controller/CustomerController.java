package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Customer;
import com.kodigo.bank.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public List<Customer> getAll() {
        return customerService.getCustomers();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getById(@PathVariable("id") Long id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveOrUpdate(@RequestBody Customer customer) {
        customerService.saveOrUpdateCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }
}
