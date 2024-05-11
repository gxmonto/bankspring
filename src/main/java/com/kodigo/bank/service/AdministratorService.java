package com.kodigo.bank.service;

import com.kodigo.bank.entity.Administrator;
import com.kodigo.bank.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministratorService {
    @Autowired
    AdministratorRepository administratorRepository;

    public Optional<Administrator> getCustomerById(Long id) {
        return administratorRepository.findById(id);
    }

    public void saveOrUpdateCustomer(Administrator administrator) {
        administratorRepository.save(administrator);
    }

    public void deleteAdministrator(Long id) {
        administratorRepository.deleteById(id);
    }
}

