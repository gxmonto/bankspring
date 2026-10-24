package com.kodigo.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtmReceipt(String reference, String operation, BigDecimal amount, String accountNumber,
                         String holderName, String atmCode, LocalDateTime date) {
}
