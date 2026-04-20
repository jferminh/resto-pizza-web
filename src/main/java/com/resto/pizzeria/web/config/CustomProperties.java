package com.resto.pizzeria.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

/**
 * Propriétés de configuration de l'application.
 */
@Configuration
@ConfigurationProperties(prefix = "com.resto.pizzeria.web")
@Data
public class CustomProperties {
  private String apiUrl;
}
