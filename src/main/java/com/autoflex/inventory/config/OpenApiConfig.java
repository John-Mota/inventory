package com.autoflex.inventory.config;

import io.r.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Autoflex Inventory API")
                        .version("1.0")
                        .description("API para controle de estoque e sugestão de produção da Autoflex.")
                        .contact(new Contact()
                                .name("Autoflex Team")
                                .email("contact@autoflex.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
