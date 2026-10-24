package com.kodigo.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record AtmWithdrawalRequest(
        @NotBlank @Pattern(regexp = "\\d{8}", message = "must be 8 digits") String code,
        @NotNull BigDecimal amount) {
}
