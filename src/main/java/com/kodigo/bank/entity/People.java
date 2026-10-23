package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "People")
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String address;
    private String contact;
    private String password;
    private String username;
    @OneToMany(mappedBy = "people")
    private List<Customer> customers;
}
