# Obligatorio_BDII

Proyecto para el obligatorio de BDII.

## Tecnologias

- Frontend: React, Vite y CSS.
- Backend: Java 21, Spring Boot y JDBC.

## Estructura

```text
backend/
  src/main/java/com/obligatorio/bdii/
  src/main/resources/
frontend/
  src/
  public/
```

## Backend

Desde la carpeta `backend`:

```bash
mvn spring-boot:run
```

El backend levanta por defecto en `http://localhost:8080`.

Endpoint inicial:

```text
GET /api/health


## Frontend

Desde la carpeta `frontend`:

```bash
npm install
npm run dev
```

El frontend levanta por defecto en `http://localhost:5173`.
