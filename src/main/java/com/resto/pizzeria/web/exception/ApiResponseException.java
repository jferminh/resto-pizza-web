package com.resto.pizzeria.web.exception;

import lombok.Getter;

import java.io.IOException;

/**
 * Exception levée en cas d'erreur de réponse de l'API.
 */
@Getter
public final class ApiResponseException extends IOException {
    private final Integer code;
    private final String codeExtended;

    /**
     * Construit une exception avec message, code étendu et code HTTP.
     *
     * @param message message d'erreur
     * @param codeExtended code d'erreur étendu
     * @param code code HTTP
     */
    public ApiResponseException(
            final String message,
            final String codeExtended,
            final Integer code) {
        super(message);
        this.code = code;
        this.codeExtended = codeExtended;
    }

    /**
     * Construit une exception avec message et code HTTP.
     *
     * @param message message d'erreur
     * @param code code HTTP
     */
    public ApiResponseException(
            final String message,
            final Integer code) {
        this(message, "", code);
    }
}