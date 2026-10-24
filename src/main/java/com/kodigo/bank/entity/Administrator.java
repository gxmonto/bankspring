package com.kodigo.bank.entity;

import jakarta.persistence.*;
import lombok.Data;

// Un administrador es una persona (People) con permisos extra; comparte su id.
@Data
@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @Column(name = "admin_id")
    private Long adminId;

    @ManyToOne
    @JoinColumn(name = "admin_id", insertable = false, updatable = false)
    private People people;
}
