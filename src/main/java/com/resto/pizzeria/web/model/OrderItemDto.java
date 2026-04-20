package com.resto.pizzeria.web.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objet de transfert de données pour un élément de commande.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;

    /** Commande associée */
    @NotNull(message = "La commande est obligatoire")
    private OrderDto order;

    /** Plat associé */
    @NotNull(message = "Le plat est obligatoire")
    private DishDto dish;

    /** Quantité du plat */
    @NotNull(message = "Le quantité est obligatoire")
    private int quantity;
}
