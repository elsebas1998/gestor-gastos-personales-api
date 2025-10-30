package com.jsca.gestor_gastos_personales_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GestorGastosPersonalesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestorGastosPersonalesApiApplication.class, args);
	}

}
