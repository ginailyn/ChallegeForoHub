-- Crear tabla: usuarios
CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    apellido VARCHAR(255),
    email VARCHAR(255),
    enabled BIT,
    nombre VARCHAR(255),
    password VARCHAR(255),
    role ENUM('ADMINISTRADOR','USUARIO'),
    username VARCHAR(255),
    PRIMARY KEY (id)
);