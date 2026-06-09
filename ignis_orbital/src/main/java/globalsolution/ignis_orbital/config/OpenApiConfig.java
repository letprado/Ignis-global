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
                        .description("""
                                O Ignis Orbital é uma plataforma de monitoramento de queimadas que recebe \
                                telemetria de satélites, converte os dados brutos em alertas georreferenciados \
                                e classifica o risco de cada foco de calor por região.

                                A API centraliza o domínio de alertas, regiões e telemetria. O processamento \
                                de payloads pode ocorrer de forma síncrona ou assíncrona via fila de mensagens, \
                                e os alertas são enriquecidos com contexto climático a partir de uma fonte \
                                meteorológica externa. O acesso é controlado por autenticação JWT, com perfis \
                                de uso distintos (administração, análise, operação e visualização).

                                Principais recursos: cadastro e consulta de regiões monitoradas, ingestão e \
                                processamento de telemetria, geração e acompanhamento de alertas com histórico \
                                de status, índice de monitoramento por região e um assistente de consulta em \
                                linguagem natural sobre os dados da plataforma.""")
                        .version("1.0.0")
                        .contact(new Contact().name("Letícia Sousa Prado (RM 559258) - Squad Ignis Orbital")))
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
