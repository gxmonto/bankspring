package com.kodigo.bank.config;

import com.kodigo.bank.entity.*;
import com.kodigo.bank.repository.*;
import com.kodigo.bank.util.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

// Datos de demostracion: se crean solo si la base esta vacia (la primera vez que arranca).
// Se desactiva con bank.demo-data.enabled=false (los tests lo hacen).
@Component
@ConditionalOnProperty(name = "bank.demo-data.enabled", havingValue = "true", matchIfMissing = true)
public class DemoDataInitializer implements CommandLineRunner {
    public static final String DEMO_ATM_ID = "ATM-001";
    public static final String DEMO_ATM_KEY = "demo-atm-key-001";

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    @Autowired
    private PeopleRepository peopleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AdministratorRepository administratorRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private AtmRepository atmRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (peopleRepository.count() > 0) {
            return;
        }
        Bank bank = new Bank();
        bank.setBankName("e-Bank");
        bank.setAddress("San Salvador");
        bankRepository.save(bank);

        People gustavo = person("Gustavo", "Montoya", "gustavo", "demo123");
        People maria = person("Maria", "Lopez", "maria", "demo123");
        People admin = person("Admin", "e-Bank", "admin", "admin123");

        account(gustavo, "1000-0001", "2500.00");
        account(maria, "1000-0002", "1200.00");

        Administrator administrator = new Administrator();
        administrator.setAdminId(admin.getId());
        administrator.setPeople(admin);
        administratorRepository.save(administrator);

        atm(DEMO_ATM_ID, "Centro - Sucursal principal", DEMO_ATM_KEY);
        atm("ATM-002", "Centro comercial", "demo-atm-key-002");

        log.info("Demo data created. Users: gustavo/demo123, maria/demo123, admin/admin123. ATM: {} / {}",
                DEMO_ATM_ID, DEMO_ATM_KEY);
    }

    private People person(String name, String surname, String username, String password) {
        People people = new People();
        people.setName(name);
        people.setSurname(surname);
        people.setUsername(username);
        people.setPassword(passwordEncoder.encode(password));
        return peopleRepository.save(people);
    }

    private void account(People people, String accountNumber, String openingBalance) {
        Customer customer = new Customer();
        customer.setPeople(people);
        customer.setAccountNumber(accountNumber);
        customer.setAccountType("SAVINGS");
        customer.setBalance(new BigDecimal(openingBalance));
        customerRepository.save(customer);
        transactionRepository.save(new Transaction(TransactionType.DEPOSIT, customer.getBalance(), customer.getBalance(),
                "Opening deposit", customer));
    }

    private void atm(String code, String location, String key) {
        Atm atm = new Atm();
        atm.setAtmCode(code);
        atm.setLocation(location);
        atm.setApiKeyHash(Hashing.sha256(key));
        atm.setActive(true);
        atmRepository.save(atm);
    }
}
