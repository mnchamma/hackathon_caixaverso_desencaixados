# Acessibilidade API

API desenvolvida para hackathon com o objetivo de persistir preferencias de acessibilidade de usuarios corporativos e registrar auditoria de alteracoes.

## Tecnologias

- Java 21
- Spring Boot 3
- Maven
- PostgreSQL
- Docker Compose
- BCrypt para senha
- JWT para autenticacao
- Swagger/OpenAPI para documentacao interativa

## Funcionalidades

- Cadastro de perfil de usuario
- Validacao de e-mail corporativo `@caixa.gov.br`
- Senha criptografada com BCrypt
- Login com token JWT
- Persistencia de preferencias de acessibilidade
- Auditoria de cadastro, login e alteracoes de perfil
- Tratamento padronizado de erros HTTP
- CORS configuravel para integracao com front Angular

## Como rodar

Suba o PostgreSQL:

```powershell
docker compose up -d
```

Inicie a API:

```powershell
mvn spring-boot:run
```

A API roda em:

```text
http://localhost:8080
```

A documentacao interativa fica em:

```text
http://localhost:8080/swagger-ui.html
```

## Usuario padrao para testes

Ao iniciar a API com o banco configurado, o sistema cria automaticamente um usuario padrao caso ele ainda nao exista:

```text
login: usuario@caixa.gov.br
senha: SenhaForte123
```

Esse usuario foi criado para que a banca avaliadora consiga realizar testes de usabilidade no sistema sem depender do fluxo de cadastro manual. A criacao acontece apenas quando o registro ainda nao existe no banco, evitando duplicidade nas proximas execucoes.

## Configuracoes

As principais configuracoes ficam em `src/main/resources/application.yaml`.

Variaveis de ambiente suportadas:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
JWT_SECRET
APP_CORS_ALLOWED_ORIGIN
```

Por padrao, o front liberado no CORS e:

```text
http://localhost:4200
```

## Endpoints

### Criar perfil

```http
POST /api/v1/perfis
```

Body:

```json
{
  "email": "usuario@caixa.gov.br",
  "senha": "SenhaForte123",
  "tamanhoTexto": 16,
  "contraste": true,
  "aparencia": false,
  "espacamento": 1.5,
  "guiaLeitura": true,
  "navegTeclado": true
}
```

### Login

```http
POST /api/v1/auth/login
```

Body:

```json
{
  "email": "usuario@caixa.gov.br",
  "senha": "SenhaForte123"
}
```

Resposta inclui o token JWT:

```json
{
  "mensagem": "Login realizado com sucesso.",
  "token": "...",
  "tokenExpiraEm": "2026-05-20T16:45:00",
  "email": "usuario@caixa.gov.br",
  "tamanhoTexto": 16,
  "contraste": true,
  "aparencia": false,
  "espacamento": 1.5,
  "guiaLeitura": true,
  "navegTeclado": true
}
```

### Consultar perfil

```http
POST /api/v1/perfis/consultar
Authorization: Bearer TOKEN
```

Body:

```json
{
  "email": "usuario@caixa.gov.br"
}
```

### Atualizar preferencias

```http
PUT /api/v1/perfis
Authorization: Bearer TOKEN
```

Body:

```json
{
  "email": "usuario@caixa.gov.br",
  "tamanhoTexto": 18,
  "contraste": true,
  "aparencia": true,
  "espacamento": 2.0,
  "guiaLeitura": false,
  "navegTeclado": true
}
```

### Consultar auditoria

```http
POST /api/v1/perfis/auditoria
Authorization: Bearer TOKEN
```

Body:

```json
{
  "email": "usuario@caixa.gov.br"
}
```

## Erros

Os erros retornam um corpo padronizado:

```json
{
  "timestamp": "2026-05-20T16:30:00",
  "status": 401,
  "erro": "Unauthorized",
  "detalhes": [
    "Token invalido."
  ]
}
```
