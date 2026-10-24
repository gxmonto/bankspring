package com.kodigo.bank.dto;

import java.math.BigDecimal;

// balance: dinero en la cuenta. reserved: apartado por codigos de retiro activos.
// available: lo que se puede usar (balance - reserved).
public record AccountSummary(String accountNumber, String holderName, BigDecimal balance,
                             BigDecimal reserved, BigDecimal available) {
}
