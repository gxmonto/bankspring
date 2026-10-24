package com.kodigo.bank.service;

import com.kodigo.bank.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

// Bloquea la fila de una cuenta hasta que termine la transaccion (SELECT ... FOR UPDATE),
// para que dos operaciones simultaneas no gasten el mismo saldo.
// Usa refresh y no un find: si la cuenta ya estaba cargada, un find devolveria el objeto
// en memoria con el saldo viejo; refresh vuelve a leerlo de la base ya bloqueado.
@Component
public class AccountLocker {
    @PersistenceContext
    private EntityManager entityManager;

    public Customer lock(Customer customer) {
        entityManager.refresh(customer, LockModeType.PESSIMISTIC_WRITE);
        return customer;
    }
}
