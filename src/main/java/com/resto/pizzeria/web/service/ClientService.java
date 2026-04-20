package com.resto.pizzeria.web.service;

import com.resto.pizzeria.web.exception.ApiValidationException;
import com.resto.pizzeria.web.model.ClientDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

/**
 * Service de gestion des clients via une API REST.
 */
@Service
public class ClientService {
    private final RestTemplate restTemplate;

    @Value("${com.resto.pizzeria.web.apiUrl}")
    private String apiBaseUrl;

    public ClientService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère tous les clients.
     *
     * @return liste des clients
     */
    public List<ClientDto> getAllClients() {
        String url = apiBaseUrl + "/clients";

        final ResponseEntity<ClientDto[]> response =
                restTemplate.getForEntity(url, ClientDto[].class);

        return Arrays.asList(response.getBody());
    }

    /**
     * Récupère un client par son identifiant.
     *
     * @param id identifiant du client
     * @return le client correspondant
     */
    public ClientDto getClientById(final Long id) {
        return restTemplate.getForObject(
                apiBaseUrl + "/clients/" + id,
                ClientDto.class
        );
    }

    /**
     * Crée un nouveau client.
     *
     * @param client client à créer
     */
    public void createClient(
            final ClientDto client
    ) throws ApiValidationException {
        restTemplate.postForObject(
                apiBaseUrl + "/clients",
                client,
                ClientDto.class
        );
    }

    /**
     * Met à jour le client.
     * @param id Identifiant du client.
     * @param client Client pour mettre à jour.
     */
    public void updateClient(
            final Long id,
            final ClientDto client) {
        restTemplate.put(
                apiBaseUrl + "/clients/" + id,
                client
        );
    }

    /**
     * Supprimer le client.
     * @param id Identifiant du client.
     */
    public void deleteClient(final Long id) {
        restTemplate.delete(
                apiBaseUrl + "/clients/" + id
        );
    }
}