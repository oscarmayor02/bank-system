-- Crear bases de datos
CREATE DATABASE cliente;
CREATE DATABASE banco;

-- =========================
-- Esquema para CLIENTE
-- =========================
\c cliente;

CREATE TABLE personas (
                          id SERIAL PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          genero VARCHAR(10),
                          edad INT CHECK (edad >= 18),
                          identificacion VARCHAR(50) UNIQUE NOT NULL,
                          direccion VARCHAR(150),
                          telefono VARCHAR(15)
);

CREATE TABLE clientes (
                          id SERIAL PRIMARY KEY,
                          cliente_id VARCHAR(50) UNIQUE NOT NULL,
                          email VARCHAR(100) UNIQUE NOT NULL,
                          contrasena VARCHAR(255) NOT NULL,
                          estado BOOLEAN DEFAULT TRUE,
                          persona_id INT REFERENCES personas(id) ON DELETE CASCADE
);

-- =========================
-- Esquema para BANCO
-- =========================
\c banco;

CREATE TABLE cuentas (
                         id SERIAL PRIMARY KEY,
                         numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
                         tipo VARCHAR(20) NOT NULL,
                         saldo_inicial NUMERIC(15,2) NOT NULL,
                         estado BOOLEAN DEFAULT TRUE,
                         cliente_id BIGINT NOT NULL
);

CREATE TABLE movimientos (
                             id SERIAL PRIMARY KEY,
                             fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             tipo_movimiento VARCHAR(20) NOT NULL,
                             valor NUMERIC(15,2) NOT NULL,
                             saldo NUMERIC(15,2) NOT NULL,
                             cuenta_id INT REFERENCES cuentas(id) ON DELETE CASCADE
);
