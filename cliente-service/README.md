# 🧑‍💻 Cliente Service

Microservicio encargado de la **gestión de clientes** para el sistema bancario.  
Permite crear, consultar, actualizar y eliminar clientes, validando reglas de negocio como:

- 📧 Email único y con formato válido
- 🔑 Contraseña encriptada con BCrypt
- 🔞 Edad mínima de 18 años
- 📱 Teléfono con 10 dígitos

Se comunica con otros microservicios (**cuentas** y **movimientos**) mediante **Feign Client** y utiliza **JWT** para autenticación.

---

## 🚀 Tecnologías usadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Lombok
- Feign Client (para comunicación entre microservicios)
- Docker & Docker Compose
- JUnit 5 + Mockito + MockMvc (pruebas)

---

## 🏗️ Diseño del paquete

cliente-service
│── src/main/java/org/clienteservice
│ ├── ClienteServiceApplication.java # Clase principal
│ │
│ ├── config
│ │ ├── JwtFilter.java
│ │ ├── JwtUtil.java
│ │ └── SecurityConfig.java
│ │
│ ├── controller
│ │ └── ClienteController.java
│ │
│ ├── dto
│ │ └── AuthRequest.java / AuthResponse.java
│ │
│ ├── entity
│ │ ├── Persona.java
│ │ └── Cliente.java
│ │
│ ├── exception
│ │ ├── ResourceNotFoundException.java
│ │ ├── EmailAlreadyExistsException.java
│ │ ├── InvalidCredentialsException.java
│ │ └── GlobalExceptionHandler.java
│ │
│ ├── repository
│ │ └── ClienteRepository.java
│ │
│ ├── service
│ │ ├── ClienteService.java
│ │ └── impl/ClienteServiceImpl.java
│ │
│ └── util
│ └── PasswordUtil.java
│
└── src/test/java/org/clienteservice
├── ClienteServiceTest.java
└── ClienteControllerIntegrationTest.java

yaml
Copiar código

---

## 🌐 Endpoints expuestos

| Método | Endpoint              | Descripción                          | Autenticación |
|--------|-----------------------|--------------------------------------|---------------|
| POST   | `/api/auth/login`     | Login de cliente y generación de JWT | ❌ No         |
| POST   | `/api/clientes`       | Registrar nuevo cliente              | ❌ No         |
| GET    | `/api/clientes`       | Listar clientes                      | ✅ Sí         |
| GET    | `/api/clientes/{id}`  | Obtener cliente por ID               | ✅ Sí         |
| PUT    | `/api/clientes/{id}`  | Actualizar cliente                   | ✅ Sí         |
| PATCH  | `/api/clientes/{id}`  | Actualización parcial                | ✅ Sí         |
| DELETE | `/api/clientes/{id}`  | Eliminar cliente                     | ✅ Sí         |

---

## ⚙️ Configuración de `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cliente
    username: postgres
    password: admin
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: 12345678901234567890123456789012
  expiration: 3600000
```
---
🐳 Dockerfile
dockerfile
Copiar código
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/cliente-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
---
📦 Ejemplo de uso en Postman
1️⃣ Crear un cliente (sin token)
http
Copiar código
POST http://localhost:8081/api/clientes
Content-Type: application/json

{
  "nombre": "Jose Lema",
  "edad": 25,
  "email": "jose@test.com",
  "contrasena": "1234",
  "telefono": "098254785"
}
2️⃣ Login para obtener JWT
http
Copiar código
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "email": "jose@test.com",
  "contrasena": "1234"
}
Respuesta:

json
Copiar código
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
3️⃣ Listar clientes (requiere token)
http
Copiar código
GET http://localhost:8081/api/clientes
Authorization: Bearer <TOKEN>
🧪 Ejecutar pruebas
Unitarias e integración:
bash
Copiar código
mvn clean test
Ver cobertura:
bash
Copiar código
mvn clean verify
🏃 Ejecución local
bash
Copiar código
mvn clean package -DskipTests
java -jar target/cliente-service-0.0.1-SNAPSHOT.jar
🐳 Ejecutar con Docker
bash
Copiar código
docker build -t cliente-service .
docker run -p 8081:8081 cliente-service
🔗 Integración con otros microservicios
Cuentas Service consulta a Cliente Service vía Feign Client.

Este micro expone /api/clientes/{id} para ser consumido desde cuentas/movimientos.