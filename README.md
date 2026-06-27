# Obligatorio BDII — Sistema de Entradas para Eventos Deportivos

- Frontend: React, Vite y CSS.
- Backend: Java 21, Spring Boot y JDBC.

```bash
# 1. Levantar MySQL
docker compose up -d

# 2. Backend (puerto 8080)
cd backend && mvn spring-boot:run

# 3. Frontend (puerto 5175)
cd frontend && npm install && npm run dev
```

## Credenciales de prueba

| Rol           | Email                  | Contraseña  |
| ------------- | ---------------------- | ----------- |
| Admin de sede | juan.perez@email.com   | password123 |
| Funcionario   | sofia.ramos@email.com  | password123 |
| Usuario       | maria.garcia@email.com | password123 |
