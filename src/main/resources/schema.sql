-- Crear la base de datos "tarea2" solo si no existe
CREATE DATABASE IF NOT EXISTS tarea2 CHARACTER SET utf8 COLLATE utf8_general_ci;

-- Usar la base de datos "tarea2"
USE tarea2;

-- Crear la tabla de usuarios si no existe
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL
);

-- Crear la tabla de roles si no existe
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(64) NOT NULL UNIQUE
);

-- Crear la tabla intermedia si no existe
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT,
    rol_id BIGINT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);