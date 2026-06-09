package com.maxiamikel19.birthday_notifier_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Birthday Notifier API")
                        .version("v1.0")
                        .description("""
                                 API REST para la gestión de miembros y notificaciones de cumpleaños.

                                Funcionalidades:
                                - CRUD de miembros
                                - Búsqueda paginada
                                - Filtros dinámicos
                                - Notificaciones de cumpleaños
                                """)
                        .contact(new Contact()
                                .name("Maxi Amikel")
                                .email("maxloversist@gmail.com"))
                        .license(new License()
                                .name("MIT")));
    }
}
