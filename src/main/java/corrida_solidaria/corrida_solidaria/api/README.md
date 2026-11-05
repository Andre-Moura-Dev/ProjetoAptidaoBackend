# Corrida Solidária - API REST

API REST desenvolvida em Java 21 e Spring Boot 3 para gerenciamento de inscrições da Corrida Solidária.

## Tecnologias
- Java 21
- Spring Boot 3
- Maven
- Spring Data JPA
- MySQL

## Como executar

**Pré-requisitos:**
- JDK 21 instalado.

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**
```bash
./mvnw spring-boot:run
```

A aplicação será iniciada na porta 8080.

## Endpoints

Prefixo: `/api/inscricoes`

GET	/api/inscricoes/listar	Lista todas as inscrições
POST	/api/inscricoes/cadastrar	Cadastra uma inscrição
PUT	/api/inscricoes/atualizar/{email}	Atualiza uma inscrição
DELETE	/api/inscricoes/cancelar/{email}	Cancela uma inscrição
GET	/api/inscricoes/buscar-por-email/{email}	Busca inscrição por e-mail
GET	/api/inscricoes/resumo	Retorna o resumo das inscrições
