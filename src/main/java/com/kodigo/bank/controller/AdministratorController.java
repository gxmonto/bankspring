package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Administrator;
import com.kodigo.bank.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/administrators")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @GetMapping
    public List<Administrator> getAll() {
        return administratorService.getAdministrators();
    }

    @GetMapping("/{id}")
    public Optional<Administrator> getById(@PathVariable("id") Long id) {
        return administratorService.getAdministratorById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveOrUpdate(@RequestBody Administrator administrator) {
        administratorService.saveOrUpdateCustomer(administrator);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        administratorService.deleteAdministrator(id);
    }
}
