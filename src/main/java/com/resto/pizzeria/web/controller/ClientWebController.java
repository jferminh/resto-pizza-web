package com.resto.pizzeria.web.controller;

import com.resto.pizzeria.web.controller.utils.BindingUtils;
import com.resto.pizzeria.web.exception.ApiValidationException;
import com.resto.pizzeria.web.model.ClientDto;
import com.resto.pizzeria.web.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Contrôleur web pour la gestion des clients.
 */
@Controller // Attention : @Controller et non @RestController pour renvoyer du HTML
@RequestMapping("/clients")
public class ClientWebController {
    private final ClientService clientService;

    public ClientWebController(final ClientService clientService) {
        this.clientService = clientService;
    }

  /**
   * READ : Affiche la page avec la liste de tous les clients.
   */
  @GetMapping
  public String listClients(final Model model) {
    model.addAttribute("clients", clientService.getAllClients());

    // On renvoie vers le fichier src/main/resources/templates/clients/list.html
    return "pages/client/list";
  }

  /**
   * CREATE (Étape 1) : Affiche le formulaire
   * vide pour ajouter un client.
   */
  @GetMapping("/new")
  public String showCreateForm(final Model model) {
    // On envoie un objet vide à Thymeleaf pour qu'il puisse lier les champs du formulaire
    model.addAttribute("client", new ClientDto());
    return "pages/client/form"; // Renvoie vers templates/clients/form_remove.html
  }

  /**
   * CREATE (Étape 2) : Réceptionne les données
   * du formulaire et les envoie à l'API.
   */
  @PostMapping
  public String createClient(
      @Valid @ModelAttribute("client") final ClientDto client,
      final BindingResult bindingResult
  ) throws Exception {
    // 1. Vérification des erreurs de validation (les @NotBlank du DTO)
    if (bindingResult.hasErrors()) {
      // S'il y a une erreur, on recharge la page du formulaire avec les messages d'erreur
      return "pages/client/form";
    }

    try {
        clientService.createClient(client);
    } catch (ApiValidationException ex) {
        BindingUtils.bindErrors(bindingResult, ex);

        return "pages/client/form";
    }

    // 3. On redirige l'utilisateur vers la page de la liste des clients
    return "redirect:/clients";
  }

  /**
   * UPDATE (Étape 1) : Affiche le formulaire
   * pré-rempli pour modifier un client.
   */
  @GetMapping("/{id}/edit")
  public String showUpdateForm(
          @PathVariable final Long id,
          final Model model) {
    // 1. On va chercher les infos du client actuel dans l'API
    model.addAttribute("client", clientService.getClientById(id));
    return "pages/client/form";
  }

  /**
   * UPDATE (Étape 2) : Réceptionne le formulaire
   * modifié et fait un PUT vers l'API.
   */
  @PostMapping("/{id}")
  public String updateClient(
      @PathVariable final Long id,
      @Valid @ModelAttribute("client") final ClientDto client,
      final BindingResult bindingResult
  ) {
    // Vérification des erreurs de validation
    if (bindingResult.hasErrors()) {
      return "pages/client/form";
    }

    // On envoie la requête PUT à l'API avec les nouvelles données
      try {
          clientService.updateClient(id, client);
      } catch (ApiValidationException ex) {
          BindingUtils.bindErrors(bindingResult, ex);
          return "pages/client/form";
      }

    return "redirect:/clients";
  }

  /**
   * DELETE : Demande à l'API de supprimer un client.
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public String deleteClient(@PathVariable Long id) {
    // On envoie la requête DELETE à l'API
    clientService.deleteClient(id);

    return "redirect:/clients";
  }
}