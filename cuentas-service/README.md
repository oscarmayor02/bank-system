# 💰 Cuentas & Movimientos Service

Microservicio encargado de la **gestión de cuentas bancarias y movimientos**.  
Permite crear cuentas, registrar depósitos/retiros, consultar saldos y generar reportes de estado de cuenta.

Incluye validaciones de negocio:
- ❌ No permitir retiros con saldo insuficiente (lanza excepción `SaldoNoDisponibleException`).
- 📖 Registro histórico de movimientos con actualización de saldo.
- 📊 Generación de reportes con cuentas + movimientos por rango de fechas.

Este microservicio consume el **Cliente Service** vía **Feign Client** para validar que el cliente existe.

---

## 🚀 Tecnologías usadas

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Lombok
- Feign Client
- Docker & Docker Compose
- JUnit 5 + Mockito + MockMvc (pruebas)

---

## 🏗️ Diseño del paquete

cuentas-service
│── src/main/java/org/cuentasservice
│ ├── CuentasServiceApplication.java # Clase principal
│ │
│ ├── config
│ │ ├── FeignConfig.java
│ │ ├── JwtFilter.java
│ │ ├── JwtUtil.java
│ │ └── SecurityConfig.java
│ │
│ ├── controller
│ │ ├── CuentaController.java
│ │ ├── MovimientoController.java
│ │ └── ReporteController.java
│ │
│ ├── dto
│ │ ├── CuentaReporteDTO.java
│ │ ├── MovimientoDTO.java
│ │ └── ReporteDTO.java
│ │
│ ├── entity
│ │ ├── Cuenta.java
│ │ └── Movimiento.java
│ │
│ ├── exception
│ │ ├── SaldoNoDisponibleException.java
│ │ ├── ResourceNotFoundException.java
│ │ └── GlobalExceptionHandler.java
│ │
│ ├── feign
│ │ └── ClienteFeignClient.java
│ │
│ ├── repository
│ │ ├── CuentaRepository.java
│ │ └── MovimientoRepository.java
│ │
│ ├── service
│ │ ├── CuentaService.java
│ │ ├── MovimientoService.java
│ │ ├── ReporteService.java
│ │ └── impl/
│ │ ├── CuentaServiceImpl.java
│ │ ├── MovimientoServiceImpl.java
│ │ └── ReporteServiceImpl.java
│ │
│ └── util
│ └── MapperUtil.java
│
└── src/test/java/org/cuentasservice
├── MovimientoServiceTest.java
└── ReporteControllerIntegrationTest.java

yaml
Copiar código

---

## 🌐 Endpoints expuestos

### 🏦 Cuentas
| Método | Endpoint              | Descripción                       | Autenticación |
|--------|-----------------------|-----------------------------------|---------------|
| POST   | `/api/cuentas`        | Crear cuenta para cliente         | ✅ Sí         |
| GET    | `/api/cuentas`        | Listar todas las cuentas          | ✅ Sí         |
| GET    | `/api/cuentas/{id}`   | Consultar cuenta por ID           | ✅ Sí         |
| PUT    | `/api/cuentas/{id}`   | Actualizar cuenta                 | ✅ Sí         |
| DELETE | `/api/cuentas/{id}`   | Eliminar cuenta                   | ✅ Sí         |

---

### 💳 Movimientos
| Método | Endpoint                      | Descripción                           | Autenticación |
|--------|--------------------------------|---------------------------------------|---------------|
| POST   | `/api/movimientos`            | Registrar depósito o retiro           | ✅ Sí         |
| GET    | `/api/movimientos/cuenta/{id}` | Consultar movimientos por cuenta      | ✅ Sí         |

⚠️ Si el retiro no tiene fondos suficientes → retorna error 400 con mensaje `"Saldo no disponible"`.

---

### 📊 Reportes
| Método | Endpoint              | Descripción                                  | Autenticación |
|--------|-----------------------|----------------------------------------------|---------------|
| GET    | `/api/reportes`       | Generar estado de cuenta por cliente y fechas| ✅ Sí         |

Ejemplo:
GET /api/reportes?clienteId=1&fechaInicio=2025-01-01&fechaFin=2025-12-31

yaml
Copiar código

---

## ⚙️ Configuración de `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/banco
    username: postgres
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: 12345678901234567890123456789012
  expiration: 3600000

feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
```

🐳 Dockerfile
dockerfile
Copiar código
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/cuentas-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]}
---
📦 Ejemplo de uso en Postman
1️⃣ Crear cuenta
http
Copiar código
POST http://localhost:8082/api/cuentas
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "numeroCuenta": "225487",
  "tipo": "Corriente",
  "saldoInicial": 100,
  "estado": true,
  "clienteId": 1
}
2️⃣ Registrar depósito
http
Copiar código
POST http://localhost:8082/api/movimientos
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "cuentaId": 1,
  "tipoMovimiento": "DEPOSITO",
  "valor": 600
}
3️⃣ Registrar retiro (con validación de saldo)
http
Copiar código
POST http://localhost:8082/api/movimientos
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "cuentaId": 1,
  "tipoMovimiento": "RETIRO",
  "valor": 2000
}
Respuesta:

json
Copiar código
{
  "error": "Saldo no disponible"
}
4️⃣ Generar reporte
http
Copiar código
GET http://localhost:8082/api/reportes?clienteId=1&fechaInicio=2025-01-01&fechaFin=2025-12-31
Authorization: Bearer <TOKEN>
Ejemplo de respuesta:

json
Copiar código
{
  "cliente": "1",
  "fechaInicio": "2025-01-01",
  "fechaFin": "2025-12-31",
  "cuentas": [
    {
      "numeroCuenta": "225487",
      "tipo": "Corriente",
      "saldoInicial": 100,
      "saldoDisponible": 700,
      "movimientos": [
        {
          "fecha": "2025-02-10",
          "tipoMovimiento": "DEPOSITO",
          "valor": 600,
          "saldo": 700
        }
      ]
    }
  ]
}
🧪 Ejecutar pruebas
Unitarias
bash
Copiar código
mvn test -Dtest=MovimientoServiceTest
Integración (MockMvc)
bash
Copiar código
mvn test -Dtest=ReporteControllerIntegrationTest
🏃 Ejecución local
bash
Copiar código
mvn clean package -DskipTests
java -jar target/cuentas-service-0.0.1-SNAPSHOT.jar
🐳 Ejecutar con Docker
bash
Copiar código
docker build -t cuentas-service .
docker run -p 8082:8082 cuentas-service
🔗 Integración con otros microservicios
Se conecta a Cliente Service vía Feign para validar clientes antes de abrir cuentas.

Expone reportes de movimientos y saldos para ser consumidos por otros servicios.