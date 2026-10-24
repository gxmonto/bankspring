package com.kodigo.bank.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank String toAccountNumber,
        @NotNull @DecimalMin(value = "0.01", message = "must be greater than zero") @Digits(integer = 13, fraction = 2)
        BigDecimal amount,
        @Size(max = 100) String description) {
}
