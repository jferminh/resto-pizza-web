package com.resto.pizzeria.web;

import com.resto.pizzeria.web.config.CustomProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Point d'entrée de l'application Pizzeria Web.
 */
@SpringBootApplication
@EnableConfigurationProperties({CustomProperties.class})
public class PizzeriaWebApplication {

    /**
     * Lance l'application Spring Boot.
     *
     * @param args arguments de la ligne de commande
     */
	public static void main(String[] args) {
		SpringApplication.run(PizzeriaWebApplication.class, args);
	}

}
