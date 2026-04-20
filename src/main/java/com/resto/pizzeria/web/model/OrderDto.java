package com.resto.pizzeria.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Objet de transfert de données pour une commande.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;

    /** Identifiant de la commande */
    @NotNull(message = "Le statut est obligatoire")
    private StatusDto status;

    /** Statut de la commande */
    @NotNull(message = "Le client est obligatoire")
    private ClientDto client;

    // @NotNull(message = "Le id est obligatoire")
    /** Identifiant journalier de la commande */
    private Integer dailyId;

    /** Date de création de la commande */
    private LocalDateTime creationDate;

    /** Liste des éléments de la commande */
    private List<OrderItemDto> items;
}
