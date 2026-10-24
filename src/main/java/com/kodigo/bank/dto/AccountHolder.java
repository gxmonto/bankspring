package com.kodigo.bank.dto;

// Lo que un cajero puede ver de una cuenta antes de depositar: el nombre enmascarado
public record AccountHolder(String accountNumber, String holderName) {
}
