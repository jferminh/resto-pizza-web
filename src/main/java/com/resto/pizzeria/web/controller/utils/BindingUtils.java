package com.resto.pizzeria.web.controller.utils;

import com.resto.pizzeria.web.exception.ApiValidationException;
import org.springframework.validation.BindingResult;

public class BindingUtils {
    public static void bindErrors(
            final BindingResult bindingResult,
            final ApiValidationException ex) {
        if (ex.getFieldsErrors() != null && !ex.getFieldsErrors().isEmpty()) {
            ex.getFieldsErrors().forEach((field, message) ->
                    bindingResult.rejectValue(field, "api.error", message)
            );
        } else {
            bindingResult.reject("api.error", ex.getMessage());
        }
    }
}
