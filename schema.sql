CREATE DATABASE IF NOT EXISTS asistencia_db;
USE asistencia_db;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(64) NOT NULL, -- SHA-256 en hex
    rol ENUM('ADMIN', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO'
);

CREATE TABLE IF NOT EXISTS asistencias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA') NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- admin123 / emp123 (SHA-256)
INSERT INTO usuarios (nombre, correo, contrasena, rol) VALUES
('Test Admin', 'a', SHA2('1', 256), 'ADMIN'),
('Test Empleado', 'b', SHA2('2', 256), 'EMPLEADO'),
('Luis Schleef', 'luis@empresa.cl', SHA2('admin123', 256), 'ADMIN'),
('Javiera Apeleo', 'javi@empresa.cl', SHA2('emp123', 256), 'EMPLEADO');

