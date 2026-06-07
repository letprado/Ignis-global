# Ignis Orbital - Documentação das Rotas

Guia de uso da API REST do Ignis Orbital: rotas, exemplos de requisição e perfis de acesso.

Base URL local:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

---

## Autenticação (como usar o token)

Quase todas as rotas exigem token JWT. A única rota pública é o login.

Fluxo padrão:

1. Faça login em `POST /api/auth/login`
2. Copie o `token` da resposta
3. No Swagger clique em **Authorize**
4. Cole no formato:

```text
Bearer SEU_TOKEN
```

Em requisições HTTP normais, envie o header:

```http
Authorization: Bearer SEU_TOKEN
```

---

## Perfis de Acesso

| Ação | ADMIN | ANALISTA | OPERADOR | VISUALIZADOR |
|------|:-----:|:--------:|:--------:|:------------:|
| Criar/listar usuários | ✅ | ❌ | ❌ | ❌ |
| CRUD de regiões | ✅ | ✅ | ✅ | ✅* |
| Ver alertas | ✅ | ✅ | ✅ | ✅ |
| Atualizar status de alerta | ✅ | ✅ | ✅ | ❌ |
| Processar telemetria | ✅ | ✅ | ✅ | ❌ |
| Usar IA | ✅ | ✅ | ❌ | ❌ |

### ADMIN
Pode fazer tudo: criar/listar usuários, CRUD de regiões, ver alertas, atualizar status, processar telemetria e usar IA.

### ANALISTA
Perfil técnico de análise: CRUD de regiões, ver alertas, atualizar status, processar telemetria e usar IA. Não cria usuários.

### OPERADOR
Perfil operacional: CRUD de regiões, ver alertas, atualizar status e processar telemetria. Não usa IA e não cria usuários.

### VISUALIZADOR
Perfil somente leitura: ver regiões e alertas. Não altera dados, não processa telemetria, não usa IA e não cria usuários.

> *Observação: hoje o CRUD de regiões está liberado para todos os perfis autenticados, inclusive `VISUALIZADOR`. Se quiser uma regra mais rígida, o ideal seria deixar `VISUALIZADOR` apenas com `GET`.

---

## Autenticação

### POST `/api/auth/login`

Autentica e retorna um JWT. **Rota pública.**

Requisição:

```json
{
  "email": "admin@ignis.com",
  "senha": "123"
}
```

Resposta:

```json
{
  "token": "eyJ...",
  "usuario": {
    "id": 11,
    "nome": "Admin",
    "perfil": "ADMIN"
  }
}
```

Usuário admin padrão criado automaticamente na primeira execução: `admin@ignis.com` / `123`.

---

## Usuários

Rotas protegidas: somente `ADMIN`.

### GET `/api/usuarios`

Lista todos os usuários.

```http
GET http://localhost:8080/api/usuarios
Authorization: Bearer SEU_TOKEN
```

Resposta:

```json
[
  {
    "id": 11,
    "nome": "Admin",
    "email": "admin@ignis.com",
    "perfil": "ADMIN",
    "dtCadastro": "2026-06-07T13:31:40"
  }
]
```

### GET `/api/usuarios/{id}`

Busca um usuário específico.

```http
GET http://localhost:8080/api/usuarios/11
Authorization: Bearer SEU_TOKEN
```

### POST `/api/usuarios`

Cria um usuário com perfil.

```json
{
  "nome": "Leticia Prado",
  "email": "leticia17prado@gmail.com",
  "senha": "123mudar",
  "perfil": "ANALISTA"
}
```

Perfis possíveis:

```text
ADMIN
ANALISTA
OPERADOR
VISUALIZADOR
```

Depois de criado, esse usuário já consegue fazer login normalmente.

---

## Regiões

Representam áreas monitoradas (Pantanal, Amazônia, Cerrado etc.).

### GET `/api/regioes`

Lista regiões (resposta com links HATEOAS).

```http
GET http://localhost:8080/api/regioes
Authorization: Bearer SEU_TOKEN
```

Resposta:

```json
{
  "_embedded": {
    "regioes": [
      {
        "id": 1,
        "nm_regiao": "Pantanal Sul",
        "ds_bioma": "PANTANAL",
        "nr_criticidade_base": 8,
        "sg_uf": "MS",
        "indice_monitoramento": null,
        "_links": {
          "self": { "href": "http://localhost:8080/api/regioes/1" }
        }
      }
    ]
  }
}
```

### GET `/api/regioes/{id}`

Busca uma região e calcula o índice de monitoramento.

```http
GET http://localhost:8080/api/regioes/1
Authorization: Bearer SEU_TOKEN
```

### POST `/api/regioes`

Cria região.

```json
{
  "nm_regiao": "Pantanal Sul",
  "ds_bioma": "PANTANAL",
  "nr_criticidade_base": 8
}
```

Campos:
- `nm_regiao`: nome da área
- `ds_bioma`: bioma
- `nr_criticidade_base`: número de 1 a 10
- `sg_uf`: UF opcional. Se omitida, a API infere uma UF padrão pelo bioma.

Biomas aceitos:

```text
AMAZONIA
CERRADO
PANTANAL
MATA_ATLANTICA
CAATINGA
PAMPA
```

### PUT `/api/regioes/{id}`

Atualiza região.

```json
{
  "nm_regiao": "Pantanal Sul Atualizado",
  "ds_bioma": "PANTANAL",
  "nr_criticidade_base": 9
}
```

### DELETE `/api/regioes/{id}`

Remove uma região.

```http
DELETE http://localhost:8080/api/regioes/1
Authorization: Bearer SEU_TOKEN
```

Se a região tiver alertas vinculados, o banco pode impedir por chave estrangeira.

### GET `/api/regioes/{id}/indice-monitoramento`

Calcula uma métrica composta:

```text
indice = criticidade_base * 0.4 + temperatura_media_alertas * 0.6
```

```http
GET http://localhost:8080/api/regioes/1/indice-monitoramento
Authorization: Bearer SEU_TOKEN
```

Resposta:

```json
{
  "regiaoId": 1,
  "indiceMonitoramento": 31.2,
  "descricao": "Indice = (criticidade_base * 0.4) + (temperatura_media_alertas * 0.6)"
}
```

---

## Alertas

Alertas são focos de calor detectados por satélite.

### GET `/api/alertas`

Lista alertas.

```http
GET http://localhost:8080/api/alertas
Authorization: Bearer SEU_TOKEN
```

Resposta:

```json
[
  {
    "id_alerta": 1,
    "regiao_nome": "Amazonia Central",
    "temperatura": 48.5,
    "risco": "ALTO",
    "status": "DETECTADO",
    "data_captura": "2026-05-01T14:22:00",
    "coordenadas": {
      "latitude": -3.4653,
      "longitude": -62.2159
    },
    "satelite": "NOAA-20"
  }
]
```

### GET `/api/alertas/{id}`

Busca um alerta específico.

```http
GET http://localhost:8080/api/alertas/1
Authorization: Bearer SEU_TOKEN
```

### PATCH `/api/alertas/{id}/status`

Atualiza o status do alerta e grava histórico em `GS_HISTORICO_LOG`.

```json
{
  "novoStatus": "EM_ANALISE"
}
```

Status aceitos:

```text
DETECTADO
EM_ANALISE
CONTIDO
ESCALADO
```

### GET `/api/alertas/{id}/contexto-climatico`

Chama a API externa Open-Meteo via Feign e traz o clima do ponto do alerta. Demonstra integração com cliente HTTP externo.

```http
GET http://localhost:8080/api/alertas/1/contexto-climatico
Authorization: Bearer SEU_TOKEN
```

---

## Telemetria

Telemetria é o JSON bruto recebido dos satélites.

### GET `/api/telemetria/pendentes`

Lista payloads pendentes.

```http
GET http://localhost:8080/api/telemetria/pendentes
Authorization: Bearer SEU_TOKEN
```

Resposta:

```json
[
  {
    "id": 13,
    "idSatelite": 5,
    "nomeSatelite": "GOES-16",
    "jsonData": "{\"temperatura\":44.8,...}",
    "dtRecebimento": "2026-05-15T12:00:01",
    "statusProcessamento": "PENDENTE",
    "alertaGeradoId": null
  }
]
```

### POST `/api/telemetria/{id}/processar`

Processa uma telemetria pendente e gera um alerta.

```http
POST http://localhost:8080/api/telemetria/13/processar
Authorization: Bearer SEU_TOKEN
```

O que acontece:
1. Lê o JSON
2. Pega `lat`, `lon` e `temperatura`
3. Encontra a região mais próxima
4. Classifica o risco:
   - `ALTO`: temperatura >= 45
   - `MEDIO`: temperatura >= 35
   - `BAIXO`: temperatura < 35
5. Cria um alerta
6. Marca a telemetria como `PROCESSADO`
7. Cria log histórico

### POST `/api/telemetria/{id}/processar-async`

Enfileira o processamento via RabbitMQ.

```http
POST http://localhost:8080/api/telemetria/13/processar-async
Authorization: Bearer SEU_TOKEN
```

- Se `MESSAGING_ENABLED=true`: usa RabbitMQ
- Se `false`: processa de forma síncrona via fallback

---

## IA

### POST `/api/ai/consultar`

Consulta o assistente Ignis (Spring AI com tools sobre dados reais).

```json
{
  "pergunta": "Quantos alertas de risco alto existem?"
}
```

Resposta com IA ativa:

```json
{
  "resposta": "Existem X alertas de risco alto cadastrados.",
  "iaAtiva": true
}
```

Resposta em modo offline (sem chave configurada):

```json
{
  "resposta": "Modo offline...",
  "iaAtiva": false
}
```

---

## Ordem recomendada para testar no Swagger

1. `POST /api/auth/login`
2. Autorizar com `Bearer SEU_TOKEN`
3. `POST /api/usuarios` para criar seu usuário
4. Login com o usuário novo
5. `GET /api/regioes`
6. `POST /api/regioes`
7. `GET /api/alertas`
8. `GET /api/telemetria/pendentes`
9. `POST /api/telemetria/{id}/processar`
10. `POST /api/ai/consultar`

---

## Equipe

| Nome | RM | Responsabilidade |
|------|----|------------------|
| Letícia Sousa Prado | 559258 | Java Advanced — API REST, banco de dados, deploy |
| Jennyfer Lee | 561020 | .NET e IoT |
| Ivanildo Alfredo | 560049 | Mobile (frontend), QA e DevOps |
