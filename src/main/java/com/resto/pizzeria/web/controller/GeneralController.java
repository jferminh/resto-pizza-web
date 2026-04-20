package com.resto.pizzeria.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur web pour les pages générales.
 */
@Controller
@RequestMapping("/")
public class GeneralController {
    /**
     * Affiche la page d'accueil.
     *
     * @param model modèle pour la vue
     * @return vue de la page d'accueil
     */
    @GetMapping
    public String homePage(Model model) {
        return "pages/homepage";
    }
}
