package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

// Cajero automatico registrado en el banco. Se autentica contra la API con su codigo
// y una llave secreta (de la llave solo se guarda el hash).
@Data
@Entity
@Table(name = "atm")
public class Atm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "atm_code", unique = true, nullable = false, length = 20)
    private String atmCode;

    private String location;

    @Column(name = "api_key_hash", nullable = false, length = 64)
    private String apiKeyHash;

    private boolean active;
}
