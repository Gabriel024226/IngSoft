-- Si no existen roles, crearlos
INSERT IGNORE INTO roles (nombre) VALUES ('ROLE_ADMIN');
INSERT IGNORE INTO roles (nombre) VALUES ('ROLE_USER');

-- Insertar un usuario administrador por defecto (solo si no existe)
-- Contraseña: admin123 (encriptada con BCrypt)
INSERT INTO usuarios (nombre, email, password) 
SELECT 'Administrador', 'admin@sistema.com', '$2a$10$9OBQoGUBZiSsCKg7vUaX0eKFs21QJZlJ9Ssrw5xqcX7QJeGHxUBsy'
FROM dual
WHERE NOT EXISTS (SELECT * FROM usuarios WHERE email = 'admin@sistema.com');

-- Asignar el rol de administrador (solo si no existe la relación)
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.email = 'admin@sistema.com' AND r.nombre = 'ROLE_ADMIN'
AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur 
    WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
);

-- Asignar el rol de usuario (solo si no existe la relación)
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.email = 'admin@sistema.com' AND r.nombre = 'ROLE_USER'
AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur 
    WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
);

-- Insertar un usuario regular por defecto (solo si no existe)
-- Contraseña: user123 (encriptada con BCrypt)
INSERT INTO usuarios (nombre, email, password) 
SELECT 'Usuario', 'user@sistema.com', '$2a$10$GdJTzW0WkfAmkRxBu1WWx.uQTvC0gNZ//iR9XP8b/n6LHQu1t/i4i'
FROM dual
WHERE NOT EXISTS (SELECT * FROM usuarios WHERE email = 'user@sistema.com');

-- Asignar el rol de usuario (solo si no existe la relación)
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.email = 'user@sistema.com' AND r.nombre = 'ROLE_USER'
AND NOT EXISTS (
    SELECT 1 FROM usuario_roles ur 
    WHERE ur.usuario_id = u.id AND ur.rol_id = r.id
);