package com.resto.pizzeria.web.model.error;

import com.resto.pizzeria.web.exception.remote.RemoteErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private String message;
    private RemoteErrorType codeExtended;
    private Map<String, String> fieldsErrors;
}
