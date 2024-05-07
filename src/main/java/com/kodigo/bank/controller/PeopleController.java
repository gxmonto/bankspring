package com.kodigo.bank.controller;

import com.kodigo.bank.entity.People;
import com.kodigo.bank.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/v1/people")
public class PeopleController {
    @Autowired
    private PeopleService peopleService;

    @GetMapping
    public List<People> getAll() {
        return peopleService.getPeople();
    }

    @GetMapping("/{id}")
    public Optional<People> getById(@PathVariable("id") Long id) {
        return peopleService.getPeopleById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveOrUpdate(@RequestBody People people) {
        peopleService.saveOrUpdatePeople(people);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        peopleService.deletePeople(id);
    }
}