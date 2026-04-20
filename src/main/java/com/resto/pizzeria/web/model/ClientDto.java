package com.resto.pizzeria.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objet de transfert de données pour un client.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
  private Integer id;

  /** Prénom du client */
  @NotBlank(message = "Le prénom est obligatoire")
  @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
  private String firstName;

  /** Nom du client */
  @NotBlank(message = "Le nom est obligatoire")
  @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
  private String lastName;
}
