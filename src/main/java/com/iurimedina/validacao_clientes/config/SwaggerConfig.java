package com.iurimedina.validacao_clientes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI customOpenAPI() {
		
		return new OpenAPI()
				.info(new Info()
						.title("API de validação de clientes.")
						.version("1.0")
						.description("API para gerenciar clientes e validar chaves de acesso."));
	}
}
