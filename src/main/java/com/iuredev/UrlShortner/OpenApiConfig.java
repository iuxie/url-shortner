package com.iuredev.UrlShortner;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI urlShortnerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Encurtador de URL API")
                        .description("API para criação de links curtos, consulta de estatísticas de acesso e redirecionamento.")
                        .version("v1"));
    }
}