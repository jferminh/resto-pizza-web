package com.resto.pizzeria.web.service;

import com.resto.pizzeria.web.model.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Service de gestion des commandes via une API REST.
 */
@Service
public class OrderService {
    private final RestTemplate restTemplate;

    @Value("${com.resto.pizzeria.web.apiUrl}")
    private String apiBaseUrl;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère toutes les commandes.
     *
     * @return liste des commandes
     */
    public List<OrderDto> getAllOrders() {
        ResponseEntity<OrderDto[]> response =
                restTemplate.getForEntity(apiBaseUrl + "/orders", OrderDto[].class);

        return Arrays.asList(response.getBody());
    }

    /**
     * Récupère une commande par son identifiant.
     *
     * @param id identifiant de la commande
     * @return la commande correspondante
     */
    public OrderDto getOrderById(Long id) {
        return restTemplate.getForObject(
                apiBaseUrl + "/orders/" + id,
                OrderDto.class
        );
    }
    /**
     * Crée une nouvelle commande.
     *
     * @param order commande à créer
     */
    public void createOrder(OrderDto order) {
        restTemplate.postForObject(
                apiBaseUrl + "/orders",
                order,
                OrderDto.class
        );
    }

    /**
     * Met à jour une commande existante.
     *
     * @param id identifiant de la commande
     * @param order données mises à jour
     */
    public void updateOrder(Long id, OrderDto order) {
        restTemplate.put(
                apiBaseUrl + "/orders/" + id,
                order
        );
    }

    /**
     * Supprime une commande.
     *
     * @param id identifiant de la commande
     */
    public void deleteOrder(Long id) {
        restTemplate.delete(
                apiBaseUrl + "/orders/" + id
        );
    }
}