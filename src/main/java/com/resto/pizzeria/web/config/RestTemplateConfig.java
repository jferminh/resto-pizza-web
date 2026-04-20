package com.resto.pizzeria.web.config;

import com.resto.pizzeria.web.exception.remote.RestControllerErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration du RestTemplate.
 */
@Configuration
public class RestTemplateConfig {
    /**
     * Crée un bean RestTemplate avec un gestionnaire d'erreurs personnalisé.
     *
     * @return instance de RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestControllerErrorHandler());
        return restTemplate;
    }
}