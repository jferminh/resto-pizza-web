package com.resto.pizzeria.web;

import com.resto.pizzeria.web.config.CustomProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({CustomProperties.class})
public class PizzeriaWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaWebApplication.class, args);
	}

}
