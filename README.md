# Acessibilidade API

API desenvolvida para hackathon com o objetivo de persistir preferências de acessibilidade de usuários corporativos e registrar auditoria de alterações.

## Tecnologias

- Java 21
- Spring Boot
- Maven
- PostgreSQL
- Docker Compose
- Python para manipulação administrativa do banco

## Funcionalidades

- Cadastro de perfil de usuário
- Validação de e-mail corporativo `@caixa.gov.br`
- Senha criptografada com BCrypt
- Persistência de preferências de acessibilidade
- Auditoria de alterações de perfil
- Banco PostgreSQL em Docker

## Endpoints principais

```text
POST /api/v1/perfis
GET  /api/v1/perfis/{email}
PUT  /api/v1/perfis/{email}
GET  /api/v1/perfis/{email}/auditoria
