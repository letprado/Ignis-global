package globalsolution.ignis_orbital.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${ignis.openapi.server-url:}")
    private String serverUrl;

    @Bean
    OpenAPI ignisOpenApi() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Ignis Orbital API")
                        .description("Microsservico de monitoramento de queimadas via telemetria satelital")
                        .version("1.0.0")
                        .contact(new Contact().name("Global Solution - FIAP").email("contato@ignis.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));

        if (!serverUrl.isBlank()) {
            openAPI.addServersItem(new Server().url(serverUrl));
        }

        return openAPI;
    }
}
