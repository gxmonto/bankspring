package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_Id")
    private Long adminId;

    @OneToOne
    @JoinColumn(name = "admin_Id")
    private People people;
}
