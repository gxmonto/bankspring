package com.kodigo.bank.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// La columna `type` en la base es ENUM('deposit','withdrawal','payment') en minusculas,
// mientras que el enum de Java esta en mayusculas.
@Converter
public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {
    @Override
    public String convertToDatabaseColumn(TransactionType type) {
        return type == null ? null : type.name().toLowerCase();
    }

    @Override
    public TransactionType convertToEntityAttribute(String value) {
        return value == null ? null : TransactionType.valueOf(value.toUpperCase());
    }
}
