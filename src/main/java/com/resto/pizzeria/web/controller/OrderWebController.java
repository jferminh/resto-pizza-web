package com.resto.pizzeria.web.controller;

import com.resto.pizzeria.web.controller.utils.BindingUtils;
import com.resto.pizzeria.web.controller.utils.OrderUtils;
import com.resto.pizzeria.web.exception.ApiValidationException;
import com.resto.pizzeria.web.model.OrderDto;
import com.resto.pizzeria.web.service.ClientService;
import com.resto.pizzeria.web.service.DishService;
import com.resto.pizzeria.web.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Contrôleur web pour la gestion des commandes.
 */
@Controller
@RequestMapping("/orders")
public class OrderWebController {
    private final OrderService orderService;
    private final ClientService clientService;
    private final DishService dishService;

    public OrderWebController(
            final OrderService orderService,
            final ClientService clientService,
            final DishService dishService) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.dishService = dishService;
    }

    /**
     * Affiche la liste des commandes.
     *
     * @param model modèle pour la vue
     * @return vue de la liste des commandes
     */
    @GetMapping
    public String listOrders(final Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "pages/order/list";
    }

    /**
     * Affiche le formulaire de création d'une commande.
     *
     * @param model modèle pour la vue
     * @return vue du formulaire
     */
    @GetMapping("/new")
    public String showCreateForm(final Model model) {
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("clients", clientService.getAllClients());
        // model.addAttribute("order", new OrderDto());

        return "pages/order/form";
    }

    /**
     * Crée une nouvelle commande.
     *
     * @param order données de la commande
     * @param bindingResult résultat de validation
     * @return redirection vers la liste des commandes
     */
    @PostMapping
    public String createOrder(
            @Valid @ModelAttribute("order") final OrderDto order,
            final BindingResult bindingResult) {
        order.setDailyId(OrderUtils.generateDailyId(LocalDate.now()));

        if (bindingResult.hasErrors()) {
            return "pages/order/form";
        }

        try {
            orderService.createOrder(order);
        } catch (ApiValidationException ex) {
            BindingUtils.bindErrors(bindingResult, ex);
            return "pages/order/form";
        }

        return "redirect:/orders";
    }

    /**
     * Affiche le formulaire de modification d'une commande.
     *
     * @param id identifiant de la commande
     * @param model modèle pour la vue
     * @return vue du formulaire
     */
    @GetMapping("/{id}/edit")
    public String showUpdateForm(
            @PathVariable final Long id,
            final Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("clients", clientService.getAllClients());

        return "pages/order/form";
    }

    /**
     * Met à jour une commande existante.
     *
     * @param id identifiant de la commande
     * @param order données mises à jour
     * @param bindingResult résultat de validation
     * @return redirection vers la liste des commandes
     */
    @PostMapping("/{id}")
    public String updateOrder(
            @PathVariable final Long id,
            @Valid @ModelAttribute("order") final OrderDto order,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/order/form";
        }

        try {
            orderService.updateOrder(id, order);
        } catch (ApiValidationException ex) {
            BindingUtils.bindErrors(bindingResult, ex);
            return "pages/order/form";
        }

        return "redirect:/orders";
    }

    /**
     * Supprime une commande.
     *
     * @param id identifiant de la commande
     * @return redirection vers la liste des commandes
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteOrder(@PathVariable final Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
}