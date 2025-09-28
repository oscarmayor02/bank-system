# 🏦 Sistema Bancario – Microservicios

Este proyecto implementa un **sistema bancario distribuido** en **arquitectura de microservicios**, que incluye:

- **Cliente Service** → gestión de clientes
- **Cuentas & Movimientos Service** → gestión de cuentas, movimientos y reportes

Ambos servicios están desacoplados, se comunican mediante **Feign Client**, y usan **JWT** para autenticación/autorización.

---

## 🚀 Tecnologías usadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Lombok
- Feign Client (comunicación entre servicios)
- Docker & Docker Compose
- JUnit 5 + Mockito + MockMvc

---

## 🏗️ Arquitectura

+-------------------+
|   Cliente Service |
|  (Puerto 8081)    |
+-------------------+
▲
│ Feign Client
▼
+---------------------------------+
|   Cuentas & Movimientos Service |
|           (Puerto 8082)         |
+---------------------------------+
│
▼
+-------------------+
|    PostgreSQL     |
|   (Puerto 5432)   |
+-------------------+
yaml
Copiar código

---

## 📦 Diseño de repositorios

- [`cliente-service`](./cliente-service) → CRUD de clientes + autenticación JWT
- [`cuentas-service`](./cuentas-service) → CRUD de cuentas, movimientos, reportes
- [`docker-compose.yml`](./docker-compose.yml) → orquestación de los servicios

---

## 🌐 Endpoints principales

### Cliente Service (`http://localhost:8081`)
- `POST /api/auth/login` → login y generación de JWT
- `POST /api/clientes` → registro de cliente (sin token)
- `GET /api/clientes/{id}` → obtener cliente por ID (requiere token)

### Cuentas & Movimientos Service (`http://localhost:8082`)
- `POST /api/cuentas` → abrir cuenta para cliente existente
- `POST /api/movimientos` → registrar depósito/retiro (valida saldo disponible)
- `GET /api/reportes` → generar estado de cuenta por cliente y rango de fechas

---

## ⚙️ Configuración con Docker Compose

Archivo `docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:13
    container_name: banco-db
    environment:
      POSTGRES_DB: banco
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: root
    ports:
      - "5432:5432"
    networks:
      - banco-net

  clientes-service:
    build: ./cliente-service
    container_name: clientes-service
    ports:
      - "8081:8081"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/cliente
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: admin
    depends_on:
      - postgres
    networks:
      - banco-net

  cuentas-service:
    build: ./cuentas-service
    container_name: cuentas-service
    ports:
      - "8082:8082"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/banco
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: root
    depends_on:
      - postgres
      - clientes-service
    networks:
      - banco-net

networks:
  banco-net:
    driver: bridge
    
```
🏃 Cómo ejecutar
1. Construir los jars
bash
Copiar código
cd cliente-service
mvn clean package -DskipTests
cd ../cuentas-service
mvn clean package -DskipTests
cd ..
# 2. Levantar con Docker Compose
bash
Copiar código
docker compose up --build
# 3. Verificar servicios
## bash
## Copiar código
### docker ps
## Deberías ver:

### clientes-service en 8081

## cuentas-service en 8082

### banco-db en 5432

# 📦 Ejemplo de flujo completo en Postman
## 1️⃣ Registrar cliente (sin token)

### http
### Copiar código
# POST http://localhost:8081/api/clientes
{
  "nombre": "Jose Lema",
  "edad": 25,
  "email": "jose@test.com",
  "contrasena": "1234",
  "telefono": "098254785"
}
### 2️⃣ Login (obtener JWT)

### http
### Copiar código
# POST http://localhost:8081/api/auth/login
{
  "email": "jose@test.com",
  "contrasena": "1234"
}
### 3️⃣ Crear cuenta (requiere token)

### http
### Copiar código
# POST http://localhost:8082/api/cuentas
Authorization: Bearer <TOKEN>
{
  "numeroCuenta": "225487",
  "tipo": "Corriente",
  "saldoInicial": 100,
  "estado": true,
  "clienteId": 1
}
4️⃣ Registrar depósito

http
Copiar código
POST http://localhost:8082/api/movimientos
Authorization: Bearer <TOKEN>
{
  "cuentaId": 1,
  "tipoMovimiento": "DEPOSITO",
  "valor": 600
}
5️⃣ Generar reporte

http
Copiar código
GET http://localhost:8082/api/reportes?clienteId=1&fechaInicio=2025-01-01&fechaFin=2025-12-31
Authorization: Bearer <TOKEN>
🧪 Ejecutar pruebas
Unitarias
bash
Copiar código
mvn test
Integración
bash
Copiar código
mvn verify
🔗 Relación entre microservicios
Cliente Service es dueño de la información de los clientes.

Cuentas Service consulta a Cliente Service vía Feign para validar el cliente antes de crear cuentas o generar reportes.

Ambos servicios validan JWT para proteger los endpoints sensibles.