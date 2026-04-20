package com.resto.pizzeria.web.controller;

import com.resto.pizzeria.web.exception.ApiResponseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gestionnaire global des exceptions pour les contrôleurs web.
 */
@ControllerAdvice
public class WebExceptionHandler {
    /**
     * Gère les exceptions de type ApiResponseException.
     *
     * @param ex exception levée
     * @param model modèle pour la vue
     * @return vue de la page d'erreur
     */
    @ExceptionHandler(ApiResponseException.class)
    public String handleApiError(
            final ApiResponseException ex,
            final Model model) {
        model.addAttribute("errorMessage", ex);

        return "pages/error";
    }
}
