package com.alura.desafios.apiForoHub.infra.errors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Clase centralizada para el manejo de errores en toda la API.
 * Utiliza @RestControllerAdvice para interceptar excepciones lanzadas en cualquier controlador.
 */
@RestControllerAdvice
public class TratamientoErrores {

    /**
     * Maneja errores de validación de campos (@Valid) en los DTOs.
     * Retorna una lista de errores con campo, valor rechazado y mensaje.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorValidacion>> error400Handler(MethodArgumentNotValidException e) {
        List<ErrorValidacion> errores = e.getFieldErrors().stream()
                .map(ErrorValidacion::new)
                .toList();
        return ResponseEntity.badRequest().body(errores);
    }

    /**
     * Maneja el caso de entidad no encontrada (EntityNotFoundException).
     * Retorna un 404 Not Found sin cuerpo.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> error404Handler() {
        return ResponseEntity.notFound().build();
    }

    /**
     * Maneja excepciones de validación de integridad (IntegrityValidation).
     * Retorna un mensaje claro con estado 400.
     */
    @ExceptionHandler(IntegrityValidation.class)
    public ResponseEntity<ErrorMensaje> errorHandlerIntegrityValidation(IntegrityValidation e) {
        return ResponseEntity.badRequest().body(new ErrorMensaje(e.getMessage()));
    }

    /**
     * Maneja excepciones de validación de negocio (ValidationException).
     * Retorna un mensaje claro con estado 400.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMensaje> errorHandlerBussinessValidation(ValidationException e) {
        return ResponseEntity.badRequest().body(new ErrorMensaje(e.getMessage()));
    }

    /**
     * Maneja cuerpos de request mal formateados (HttpMessageNotReadableException).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMensaje> invalidBodyHandler(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(
                new ErrorMensaje("Request body inválido: " + e.getMessage())
        );
    }

    /**
     * Maneja cualquier excepción no prevista.
     * Retorna un mensaje genérico con estado 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMensaje> genericHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMensaje("Ocurrió un error inesperado: " + e.getMessage()));
    }

    /**
     * Clase interna que representa un error de validación de campo.
     * Contiene el nombre del campo, valor rechazado y mensaje de error.
     */
    private record ErrorValidacion(String campo, Object valorRechazado, String mensaje) {
        public ErrorValidacion(FieldError error) {
            this(error.getField(), error.getRejectedValue(), error.getDefaultMessage());
        }
    }

    /**
     * Clase interna que representa un mensaje de error general.
     */
    private record ErrorMensaje(String mensaje) {}
}