-- Esquema inicial de la base de datos asistencias-mvp.
-- Se ejecuta automáticamente la primera vez si el archivo de BD no existe
-- (ver dbConexion). También puede ejecutarse a mano:
--   sqlite3 asistencia_db < src/main/resources/db/init.sql

CREATE TABLE roles (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE tipos_asistencia (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    correo TEXT NOT NULL UNIQUE,
    contrasena TEXT NOT NULL,
    rol_id INTEGER NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE asistencias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    tipo_id INTEGER NOT NULL,
    fecha TEXT NOT NULL,
    hora TEXT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (tipo_id) REFERENCES tipos_asistencia(id)
);

INSERT INTO roles (nombre) VALUES ('ADMIN'), ('EMPLEADO');
INSERT INTO tipos_asistencia (nombre) VALUES ('ENTRADA'), ('SALIDA');

-- Usuario admin semilla: correo luis@empresa.cl, contraseña admin123
-- (hash BCrypt). Cambiarla al primer uso.
INSERT INTO usuarios (nombre, correo, contrasena, rol_id)
VALUES ('a', 'luis@empresa.cl', '$2a$10$CQBeHwnpDwGSB7XH.MgqBOuLZDci6NeyVWphzjABTlrFH2deqOyWW', (SELECT id FROM roles WHERE nombre = 'ADMIN'));
