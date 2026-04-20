package com.resto.pizzeria.web.controller;

import com.resto.pizzeria.web.controller.utils.BindingUtils;
import com.resto.pizzeria.web.exception.ApiValidationException;
import com.resto.pizzeria.web.model.DishDto;
import com.resto.pizzeria.web.service.DishService;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur web pour la gestion des plats.
 */
@Controller
@RequestMapping("/dishes")
public class DishWebController {

    private final DishService dishService;

    public DishWebController(final DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * Affiche la liste des plats.
     *
     * @param model modèle pour la vue
     * @return vue de la liste des plats
     */
    @GetMapping
    public String listDishes(final Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        return "pages/dish/list";
    }

    /**
     * Affiche le formulaire de création d'un plat.
     *
     * @param model modèle pour la vue
     * @return vue du formulaire
     */
    @GetMapping("/new")
    public String showCreateForm(final Model model) {
        model.addAttribute("dish", new DishDto());
        return "pages/dish/form";
    }

    /**
     * Crée un nouveau plat.
     *
     * @param dish données du plat
     * @param bindingResult résultat de validation
     * @return redirection vers la liste des plats
     */
    @PostMapping
    public String createDish(
            @Valid @ModelAttribute("dish") final DishDto dish,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/dish/form";
        }

        try {
            dishService.createDish(dish);
        } catch (ApiValidationException ex) {
            BindingUtils.bindErrors(bindingResult, ex);
            return "pages/dish/form";
        }

        return "redirect:/dishes";
    }

    /**
     * Affiche le formulaire de modification d'un plat.
     *
     * @param id identifiant du plat
     * @param model modèle pour la vue
     * @return vue du formulaire
     */
    @GetMapping("/{id}/edit")
    public String showUpdateForm(
            @PathVariable final Long id,
            final Model model) {
        model.addAttribute("dish", dishService.getDishById(id));
        return "pages/dish/form";
    }

    /**
     * Met à jour un plat existant.
     *
     * @param id identifiant du plat
     * @param dish données mises à jour
     * @param bindingResult résultat de validation
     * @return redirection vers la liste des plats
     */
    @PostMapping("/{id}")
    public String updateDish(
            @PathVariable final Long id,
            @Valid @ModelAttribute("dish") final DishDto dish,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/dish/form";
        }

        try {
            dishService.updateDish(id, dish);
        } catch (ApiValidationException ex) {
            BindingUtils.bindErrors(bindingResult, ex);
            return "pages/dish/form";
        }

        return "redirect:/dishes";
    }

    /**
     * Supprime un plat.
     *
     * @param id identifiant du plat
     * @return redirection vers la liste des plats
     */
    @PostMapping("/delete/{id}")
    public String deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return "redirect:/dishes";
    }
}