# Deploy no Railway - Ignis Orbital

Guia para subir o backend Spring Boot no Railway.

## Pré-requisitos

- Conta no [Railway](https://railway.app)
- Projeto versionado no GitHub (o Railway faz deploy a partir do repositório)
- Os arquivos `Dockerfile`, `railway.json` e `.dockerignore` já estão na pasta `java/ignis_orbital`

## Passo 1 - Subir o código para o GitHub

Se ainda não tiver repositório, crie um e suba o projeto. O importante é que a pasta `java/ignis_orbital` (com o `Dockerfile`) esteja versionada.

## Passo 2 - Criar o projeto no Railway

1. Acesse https://railway.app e faça login
2. **New Project** → **Deploy from GitHub repo**
3. Selecione o repositório
4. Em **Settings → Root Directory**, defina:

```text
java/ignis_orbital
```

> Isso é essencial porque o `Dockerfile` está dentro dessa subpasta, não na raiz do repositório.

5. O Railway detecta o `Dockerfile` automaticamente (definido no `railway.json`).

## Passo 3 - Configurar as variáveis de ambiente

Em **Variables**, adicione:

| Variável | Valor | Obrigatória |
|----------|-------|:-----------:|
| `ORACLE_URL` | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl` | ✅ |
| `ORACLE_USER` | `rm559258` | ✅ |
| `ORACLE_PASSWORD` | `170904` | ✅ |
| `JWT_SECRET` | uma chave longa (mín. 32 caracteres) | ✅ |
| `OPENAI_API_KEY` | sua chave OpenAI com crédito | opcional |
| `AI_CHAT` | `openai` (só se for usar IA) | opcional |
| `MESSAGING_ENABLED` | `false` | recomendado |
| `CORS_ORIGINS` | URLs liberadas para navegador, separadas por vírgula | recomendado |
| `OPENAPI_SERVER_URL` | URL pública HTTPS do backend | recomendado |

> Não defina `PORT` manualmente — o Railway injeta automaticamente, e a aplicação já lê `server.port=${PORT:8080}`.

Para este deploy, use:

```text
CORS_ORIGINS=https://ignis-global-production.up.railway.app,http://localhost:3000,http://localhost:5173
OPENAPI_SERVER_URL=https://ignis-global-production.up.railway.app
```

Isso evita que o Swagger gere requisições em `http://...` quando a aplicação está atrás do proxy HTTPS do Railway.

## Passo 4 - Gerar o domínio público

1. Em **Settings → Networking → Public Networking**
2. Clique em **Generate Domain**
3. O Railway cria uma URL tipo `https://ignis-orbital-production.up.railway.app`

Swagger ficará em:

```text
https://SEU-DOMINIO.up.railway.app/swagger-ui.html
```

## ATENÇÃO: acesso ao Oracle da FIAP

O banco `oracle.fiap.com.br` normalmente só aceita conexões da **rede/VPN da FIAP**. Servidores externos como o Railway provavelmente **não conseguem** se conectar a esse Oracle.

Sintoma se for o caso: a aplicação sobe mas falha ao iniciar com erro de conexão (`HikariPool` / `IO Error` / timeout).

Alternativas:

1. **Banco em nuvem**: provisionar um Oracle/Postgres acessível publicamente e apontar `ORACLE_URL` para ele.
2. **Railway só para demonstração da API**: se o objetivo é mostrar a API funcionando no vídeo, considere rodar local (onde o acesso à FIAP funciona) e usar o Railway apenas se trocar para um banco acessível.

> Se quiser, dá para adaptar o projeto para Postgres (Railway oferece Postgres gerenciado com 1 clique), o que elimina o problema de rede com a FIAP.

## Passo 5 - Acompanhar o deploy

Na aba **Deployments**, veja os logs. Quando aparecer:

```text
Started IgnisOrbitalApplication in X seconds
Tomcat started on port ...
```

o deploy está no ar.
