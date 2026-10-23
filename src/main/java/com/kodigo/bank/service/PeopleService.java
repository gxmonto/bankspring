package com.kodigo.bank.service;

import com.kodigo.bank.entity.People;
import com.kodigo.bank.repository.PeopleRepository;
//import com.kodigo.bank.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    @Autowired
    PeopleRepository peopleRepository;
    //@Autowired
    //private AdministratorRepository administratorRepository;
    public List<People> getPeople() {
        return peopleRepository.findAll();
    }

    public Optional<People> getPeopleById(Long id) {
        return peopleRepository.findById(id);
    }

    public void saveOrUpdatePeople(People people) {
        peopleRepository.save(people);
    }

    public void deletePeople(Long id) {
        peopleRepository.deleteById(id);
    }

    public People findByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    //public boolean isAdmin(Long userId) {
        // Check if the userId exists in the Administrator table
        //return administratorRepository.existsByAdminId(userId);
    //}
}
