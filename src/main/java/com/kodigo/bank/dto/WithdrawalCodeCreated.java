package com.kodigo.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// La unica respuesta que incluye el codigo completo; despues ya no se puede recuperar.
public record WithdrawalCodeCreated(Long id, String code, BigDecimal amount, LocalDateTime expiresAt) {
}
