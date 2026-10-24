package com.kodigo.bank.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AtmDepositRequest(
        @NotBlank String accountNumber,
        @NotNull @DecimalMin(value = "1", message = "must be at least 1") @Digits(integer = 13, fraction = 2)
        BigDecimal amount) {
}
