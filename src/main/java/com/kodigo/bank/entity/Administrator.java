package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_Id")
    private Long adminId;

    @ManyToOne
    @JoinColumn(name = "id")
    private People people;

    @OneToMany(mappedBy = "administrator")
    private List<Administrator> administrators;
}
