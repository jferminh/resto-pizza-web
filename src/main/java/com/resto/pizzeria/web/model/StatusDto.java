package com.resto.pizzeria.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objet de transfert de données pour le statut.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {
    /** Identifiant du statut */
    private Long id;

    /** Libellé du statut */
    @NotBlank(message = "Le libellé est obligatoire")
    @Size(min = 2, max = 50, message = "Le libellé doit contenir 50 caractères maximum")
    private String label;
}