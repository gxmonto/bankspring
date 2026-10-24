package com.kodigo.bank;

import com.kodigo.bank.entity.*;
import com.kodigo.bank.repository.*;
import com.kodigo.bank.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// Datos de prueba: dos clientes, un admin y un cajero. Se recrean antes de cada test.
@Component
public class TestData {
    public static final String ATM_ID = "ATM-TEST";
    public static final String ATM_KEY = "test-key";

    @Autowired PeopleRepository peopleRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired AdministratorRepository administratorRepository;
    @Autowired TransactionRepository transactionRepository;
    @Autowired WithdrawalCodeRepository withdrawalCodeRepository;
    @Autowired AtmRepository atmRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Transactional
    public void reset() {
        withdrawalCodeRepository.deleteAllInBatch();
        transactionRepository.deleteAllInBatch();
        administratorRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        atmRepository.deleteAllInBatch();
        peopleRepository.deleteAllInBatch();

        account(person("gustavo", "Gustavo", "Montoya"), "1000-0001", "1000.00");
        account(person("maria", "Maria", "Lopez"), "1000-0002", "500.00");
        People adminPeople = person("admin", "Admin", "Bank");
        Administrator admin = new Administrator();
        admin.setAdminId(adminPeople.getId());
        admin.setPeople(adminPeople);
        administratorRepository.save(admin);

        Atm atm = new Atm();
        atm.setAtmCode(ATM_ID);
        atm.setLocation("Test");
        atm.setApiKeyHash(Hashing.sha256(ATM_KEY));
        atm.setActive(true);
        atmRepository.save(atm);
    }

    public BigDecimal balance(String accountNumber) {
        return customerRepository.findByAccountNumber(accountNumber).orElseThrow().getBalance();
    }

    private People person(String username, String name, String surname) {
        People people = new People();
        people.setUsername(username);
        people.setName(name);
        people.setSurname(surname);
        people.setPassword(passwordEncoder.encode("secret"));
        return peopleRepository.save(people);
    }

    private void account(People people, String number, String balance) {
        Customer customer = new Customer();
        customer.setPeople(people);
        customer.setAccountNumber(number);
        customer.setAccountType("SAVINGS");
        customer.setBalance(new BigDecimal(balance));
        customerRepository.save(customer);
    }
}
