package com.resto.pizzeria.web.exception;

import jakarta.validation.ValidationException;
import lombok.Getter;

import java.io.IOException;
import java.util.Map;

@Getter
public final class ApiValidationException extends ValidationException {
    final Map<String, String> fieldsErrors;

    public ApiValidationException(
            final String message,
            final Map<String, String> fieldsErrors) {
        super(message);
        this.fieldsErrors = fieldsErrors;
    }
}
