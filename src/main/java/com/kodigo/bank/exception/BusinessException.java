package com.kodigo.bank.exception;

// Una operacion que el banco rechaza por una regla de negocio (fondos insuficientes,
// codigo vencido, etc.). Se responde con 400 y el mensaje se le muestra al usuario.
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
