package com.resto.pizzeria.web.service;

import com.resto.pizzeria.web.model.DishDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Service de gestion des plats via une API REST.
 */
@Service
public class DishService {
    private final RestTemplate restTemplate;

    @Value("${com.resto.pizzeria.web.apiUrl}")
    private String apiBaseUrl;

    public DishService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère tous les plats.
     *
     * @return liste des plats
     */
    public List<DishDto> getAllDishes() {
        final String url = apiBaseUrl + "/dishes";
        final ResponseEntity<DishDto[]> response =
                restTemplate.getForEntity(url, DishDto[].class);

        return Arrays.asList(response.getBody());
    }

    /**
     * Récupère un plat par son identifiant.
     *
     * @param id identifiant du plat
     * @return le plat correspondant
     */
    public DishDto getDishById(final Long id) {
        return restTemplate.getForObject(
                apiBaseUrl + "/dishes/" + id,
                DishDto.class
        );
    }

    /**
     * Crée un nouveau plat.
     *
     * @param dish plat à créer
     */
    public void createDish(final DishDto dish) {
        restTemplate.postForObject(
                apiBaseUrl + "/dishes",
                dish,
                DishDto.class
        );
    }

    /**
     * Met à jour un plat existant.
     *
     * @param id identifiant du plat
     * @param dish données mises à jour
     */
    public void updateDish(final Long id, final DishDto dish) {
        restTemplate.put(
                apiBaseUrl + "/dishes/" + id,
                dish
        );
    }

    /**
     * Supprime un plat.
     *
     * @param id identifiant du plat
     */
    public void deleteDish(final Long id) {
        restTemplate.delete(
                apiBaseUrl + "/dishes/" + id
        );
    }
}