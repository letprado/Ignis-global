# Ignis Orbital - API Backend (DevOps & Cloud Computing)

Este repositório contém o código-fonte do backend do projeto **Ignis Orbital** (desenvolvido em Java 21 + Spring Boot e Maven), bem como as configurações da esteira de Integração e Entrega Contínuas (CI/CD) desenvolvidas para a disciplina de DevOps.

## ⚙️ Como Executar a Pipeline (CI/CD)

A pipeline foi construída utilizando o **Azure DevOps** com a abordagem de configuração via código (*Configuration as Code*). Toda a infraestrutura em nuvem roda no ecossistema da Microsoft Azure, conectada a um banco de dados **Oracle**.

1. **O Código da Pipeline:** Toda a configuração base de Integração Contínua está localizada no arquivo `azure-pipeline.yml` na raiz deste repositório, gerenciando o código que fica dentro da pasta `/ignis_orbital`.
2. **Integração Contínua (CI):** Para reproduzir, basta realizar um *Push* para a branch `main` ou disparar a pipeline manualmente no painel do Azure DevOps. A esteira provisionará uma máquina virtual, configurará o Java 21, forçará o *Build* via Maven suprimindo testes locais para evitar gargalos de credenciais, e publicará o artefato (`.jar`) final.
3. **Entrega Contínua (CD):** Após o sucesso do CI, a etapa de Release captura o artefato gerado e faz o *Deploy* automático no **Azure App Service** (Linux), disponibilizando a aplicação na nuvem atualizada em tempo real.

---

## 🎥 Vídeo Demonstrando o Funcionamento da Solução

Para visualizar o funcionamento completo da aplicação, incluindo execução da API, integração com banco de dados Oracle, pipeline CI/CD e deploy na nuvem Azure, acesse:

**🔗 Vídeo no YouTube:** [https://youtu.be/TTgom_rzO0I]

---

## 🧪 Como Testar a Aplicação

O professor pode reproduzir os testes, validar as rotas e o funcionamento da persistência no banco de dados utilizando a documentação interativa (Swagger) que já está rodando em produção na nuvem:

**🔗 Acesse o Swagger:** [https://api-ignis-orbital-app.azurewebsites.net/swagger-ui/index.html](https://api-ignis-orbital-app.azurewebsites.net/swagger-ui/index.html)
