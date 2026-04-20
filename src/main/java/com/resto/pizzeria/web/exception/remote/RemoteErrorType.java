package com.resto.pizzeria.web.exception.remote;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum RemoteErrorType {
    CODE_NOT_FOUND("CODE_NOT_FOUND"),
    CODE_NOT_VALIDATED("CODE_NOT_VALIDATED"),
    CODE_DB_CONSTRAINT_VIOLATION("CODE_DB_CONSTRAINT_VIOLATION"),
    CODE_DB_INTEGRITY_VIOLATION("CODE_DB_INTEGRITY_VIOLATION"),
    CODE_INVALID_FORMAT("CODE_INVALID_FORMAT"),
    CODE_HTTP_NOT_READABLE("CODE_HTTP_NOT_READABLE"),
    CODE_UNKNOWN("CODE_UNKNOWN");

    private final String code;

    RemoteErrorType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static RemoteErrorType fromCode(String code) {
        for (RemoteErrorType e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }

        log.error("Invalid RemoteErrorType code: {}", code);
        return CODE_UNKNOWN;
    }
}
