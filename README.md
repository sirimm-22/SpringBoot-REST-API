# SpringBoot-REST-API
A Spring Boot REST API for managing users, utilising Gradle (Kotlin DSL), JSON file-based storage, along with testing, and Swagger documentation.

## Features:
- CRUD operations for users (GET, POST, PUT, DELETE)
- Data validation using @Valid and @NotNull
- File-based storage (users.json) instead of a database
- Unit & Integration tests with JUnit/Mockito
- API documentation with Swagger
- Tested using Postman 

## Tech-stack used:
- Spring Boot (Java, Gradle - Kotlin DSL)
- JUnit, Mockito (Testing)
- Swagger (API Docs)


## Running Application Locally

In your terminal, from within repository root directory, run:

```shell
./gradlew bootRun
```

This command provisions local development infrastructure and starts a development server
at http://localhost:8082.

## API Docs 

visit `http://localhost:8082/swagger-ui.html`

<img width="1725" alt="Screenshot 2025-02-09 at 6 24 08 PM" src="https://github.com/user-attachments/assets/d201ec13-d6a2-4a27-a7bd-ef0759d96cf5" />

## Running Testcases

```shell
./gradlew test
```
