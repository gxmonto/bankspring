package com.kodigo.bank.service;

import com.kodigo.bank.entity.People;
import com.kodigo.bank.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    @Autowired
    PeopleRepository peopleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public List<People> getPeople() {
        return peopleRepository.findAll();
    }

    public Optional<People> getPeopleById(Long id) {
        return peopleRepository.findById(id);
    }

    // Las contraseñas siempre se guardan cifradas con BCrypt. Si al actualizar
    // no se manda contraseña, se conserva la anterior.
    public void saveOrUpdatePeople(People people) {
        if (people.getPassword() == null || people.getPassword().isBlank()) {
            people.setPassword(Optional.ofNullable(people.getId())
                    .flatMap(peopleRepository::findById)
                    .map(People::getPassword)
                    .orElse(null));
        } else {
            people.setPassword(passwordEncoder.encode(people.getPassword()));
        }
        peopleRepository.save(people);
    }

    public void deletePeople(Long id) {
        peopleRepository.deleteById(id);
    }
}
