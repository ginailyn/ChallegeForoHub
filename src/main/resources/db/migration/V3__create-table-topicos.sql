CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    estado ENUM('CLOSED','DELETED','OPEN'),
    fecha_creacion DATETIME(6),
    mensaje VARCHAR(255),
    titulo VARCHAR(255),
    ultima_actualizacion DATETIME(6),
    curso_id BIGINT,
    usuario_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (curso_id) REFERENCES cursos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);