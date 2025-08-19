CREATE TABLE respuestas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    borrado BIT,
    fecha_creacion DATETIME(6),
    mensaje VARCHAR(255),
    solucion BIT,
    ultima_actualizacion DATETIME(6),
    topico_id BIGINT,
    usuario_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (topico_id) REFERENCES topicos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);