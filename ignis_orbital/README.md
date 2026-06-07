# Ignis Orbital - Backend Java

Microsservico Spring Boot para monitoramento de queimadas via telemetria satelital, integrado ao Oracle Database do ecossistema Ignis Orbital.

## Stack

- Java 21 + Spring Boot 3.4
- Spring Data JPA + Oracle
- Spring Security + JWT
- HATEOAS, Cache, CORS, Swagger/OpenAPI
- RabbitMQ (mensageria assincrona)
- OpenFeign (Open-Meteo)
- Spring AI (Tooling + assistente conversacional)

## Pre-requisitos

1. Oracle FIAP com os scripts em `Banco de Dados/entrega-portal/sql/` executados no schema do aluno
2. Java 21 e Maven
3. Docker (para subir o RabbitMQ da mensageria)
4. (Opcional) `OPENAI_API_KEY` para Spring AI completo

## Configuracao

A conexao Oracle FIAP ja vem como padrao em `application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
spring.datasource.username=rm559258
spring.datasource.password=170904
```

Para sobrescrever em outra maquina, use variaveis de ambiente:

```bash
ORACLE_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
ORACLE_USER=rm559258
ORACLE_PASSWORD=170904
JWT_SECRET=chave-secreta-com-pelo-menos-32-caracteres
MESSAGING_ENABLED=true
OPENAI_API_KEY=sk-...
```

## Executar

1. Suba o RabbitMQ (mensageria):

```bash
cd java/ignis_orbital
docker compose up -d
```

Console RabbitMQ: http://localhost:15672 (guest / guest)

2. Rode a aplicacao com mensageria ativa:

```bash
# Linux/Mac
MESSAGING_ENABLED=true ./mvnw spring-boot:run

# Windows PowerShell
$env:MESSAGING_ENABLED="true"; .\mvnw.cmd spring-boot:run
```

> Sem Docker/RabbitMQ, deixe `MESSAGING_ENABLED=false` (padrao) e o processamento de telemetria roda sincronamente.

Swagger: http://localhost:8080/swagger-ui.html

## Login padrao

```json
POST /api/auth/login
{
  "email": "admin@ignis.com",
  "senha": "123"
}
```

## Endpoints principais

| Metodo | Rota | Descricao |
|--------|------|-----------|
| POST | `/api/auth/login` | Autenticacao JWT |
| GET/POST/PUT/DELETE | `/api/regioes` | CRUD de regioes (HATEOAS) |
| GET | `/api/alertas` | Listar alertas |
| PATCH | `/api/alertas/{id}/status` | Atualizar status com auditoria |
| GET | `/api/alertas/{id}/contexto-climatico` | Enriquecimento via Feign |
| POST | `/api/telemetria/{id}/processar` | Processar telemetria -> alerta |
| POST | `/api/telemetria/{id}/processar-async` | Fila RabbitMQ |
| POST | `/api/ai/consultar` | Assistente Spring AI |

## Arquitetura de microsservicos (justificativa)

O Ignis Orbital adota **microsservicos especializados**:

- **API Core (este projeto)**: dominio de alertas, regioes e telemetria
- **Banco Oracle**: persistencia relacional compartilhada
- **RabbitMQ**: desacoplamento do processamento pesado de payloads JSON
- **Open-Meteo (Feign)**: servico externo de clima sem acoplar logica interna

Isso permite escalar independentemente o processamento de telemetria e integrar novos satelites sem redeploy completo.

## Feign vs RestClient

Utilizamos **OpenFeign** por declarar contratos HTTP tipados (`OpenMeteoClient`), facilitando mocks em testes e integracao com Spring Cloud. RestClient seria equivalente, mas Feign oferece melhor legibilidade para APIs externas estaveis.

## Funcionalidades reais (alem de CRUD)

1. **Processamento de telemetria satelital** - converte JSON bruto em alertas georreferenciados
2. **Classificacao de risco** - espelha `FN_CLASSIFICAR_RISCO` do Oracle
3. **Indice de monitoramento** - metrica composta por regiao
4. **Contexto climatico** - correlaciona alerta com dados meteorologicos
5. **Assistente IA** - consultas em linguagem natural com tools sobre alertas/regioes
