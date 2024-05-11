package com.kodigo.bank.service;

import com.kodigo.bank.entity.Administrator;
import com.kodigo.bank.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {
    @Autowired
    AdministratorRepository administratorRepository;

    public List<Administrator> getAdministrators() {
        return administratorRepository.findAll();
    }

    public Optional<Administrator> getAdministrator(Long id) {
        return administratorRepository.findById(id);
    }

    public void saveOrUpdate(Administrator administrator) {
        administratorRepository.save(administrator);
    }

    public void delete(Long id) {
        administratorRepository.deleteById(id);
    }
}
