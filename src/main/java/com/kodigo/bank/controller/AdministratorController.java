package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Administrator;
import com.kodigo.bank.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/administrators")
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @GetMapping
    public List<Administrator> getAllAdministrators() {
        return administratorService.getAdministrators();
    }

    @GetMapping("/{id}")
    public Optional<Administrator> getAdministratorById(@PathVariable("admin_id") Long admin_id) {
        return administratorService.getAdministrator(admin_id);
    }

    @PostMapping
    public void saveOrUpdateAdministrator(@RequestBody Administrator administrator) {
        administratorService.saveOrUpdate(administrator);
    }

    @DeleteMapping("/{id}")
    public void deleteAdministratorById(@PathVariable("admin_id") Long admin_id) {
        administratorService.delete(admin_id);
    }
}
