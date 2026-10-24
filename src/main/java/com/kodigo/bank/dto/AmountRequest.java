package com.kodigo.bank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AmountRequest(
        @NotNull @DecimalMin(value = "0.01", message = "must be greater than zero") @Digits(integer = 13, fraction = 2)
        BigDecimal amount) {
}
