package com.kodigo.bank.security;

import com.kodigo.bank.entity.People;
import com.kodigo.bank.repository.AdministratorRepository;
import com.kodigo.bank.repository.CustomerRepository;
import com.kodigo.bank.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// Carga el usuario para el login. Los roles salen de las tablas que ya existian:
// tiene cuenta en `customer` -> CUSTOMER; esta en `administrator` -> ADMIN.
@Service
public class BankUserDetailsService implements UserDetailsService {
    @Autowired
    private PeopleRepository peopleRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        People people = peopleRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        List<String> roles = new ArrayList<>();
        if (customerRepository.findFirstByPeopleUsername(username).isPresent()) {
            roles.add("CUSTOMER");
        }
        if (administratorRepository.existsById(people.getId())) {
            roles.add("ADMIN");
        }
        return User.withUsername(people.getUsername())
                .password(people.getPassword())
                .roles(roles.toArray(String[]::new))
                .build();
    }
}
