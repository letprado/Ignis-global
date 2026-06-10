# Ignis Orbital - Backend Java

Microsservico Spring Boot para monitoramento de queimadas via telemetria satelital, integrado ao Oracle Database do ecossistema Ignis Orbital.

## Equipe

| Nome | RM | Responsabilidade |
|------|----|------------------|
| Letícia Sousa Prado | 559258 | Java Advanced — API REST, banco de dados, deploy |
| Jennyfer Lee | 561020 | .NET e IoT |
| Ivanildo Alfredo | 560049 | Mobile (frontend) e DevOps |
| Paulo Neto | 560262 | QA

## Stack

- Java 21 + Spring Boot 3.4
- Spring Data JPA + Oracle
- Spring Security + JWT
- HATEOAS, Cache, CORS, Swagger/OpenAPI
- RabbitMQ (mensageria assincrona)
- OpenFeign (Open-Meteo)
- Spring AI (Tooling + assistente conversacional)

## Acesso da API

A aplicacao esta publicada e em execucao no Railway, nao sendo necessario rodar localmente para testar a API:

- API base: https://ignis-global-production.up.railway.app
- Swagger (documentacao interativa): https://ignis-global-production.up.railway.app/swagger-ui/index.html

Caso seja necessario executar o projeto localmente, a API fica disponivel em:

- API local: http://localhost:8080
- Swagger local: http://localhost:8080/swagger-ui.html

## Video de demonstracao

Apresentacao das rotas e explicacao da solucao no YouTube:

- https://youtu.be/_LTYmmdnvNM

## Pre-requisitos

1. Java 21 e Maven
2. Docker (para subir o RabbitMQ da mensageria)
3. (Opcional) `OPENAI_API_KEY` para Spring AI completo

## Configuracao

As credenciais e segredos nao ficam no codigo: sao lidos de variaveis de ambiente. Defina-as antes de executar (no Railway ja estao configuradas no servico):

```bash
ORACLE_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
ORACLE_USER=SEU_USUARIO_ORACLE
ORACLE_PASSWORD=SUA_SENHA_ORACLE
JWT_SECRET=defina-uma-chave-secreta-com-no-minimo-32-caracteres
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
