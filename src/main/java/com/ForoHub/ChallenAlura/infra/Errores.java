package com.ForoHub.ChallenAlura.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class Errores {

    // Maneja excepciones de tipo EntityNotFoundException, devolviendo un 404 Not Found.
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> manejarError404() {
        return ResponseEntity.notFound().build();
    }

    // Maneja excepciones de validación que ocurren cuando los argumentos no son válidos (por ejemplo, anotaciones @Valid).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DatosErrorValidacion>> manejarError400(MethodArgumentNotValidException e) {
        // Recopila todos los errores de campo y los pone en una lista de DatosErrorValidacion.
        List<DatosErrorValidacion> errores = e.getBindingResult().getFieldErrors().stream()
                .map(DatosErrorValidacion::new)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errores);
    }

    // Maneja errores de formato JSON
    @ExceptionHandler({InvalidFormatException.class, JsonMappingException.class})
    public ResponseEntity<String> manejarErrorJSON(Exception e) {
        return ResponseEntity.badRequest().body("Error en el formato JSON: " + e.getMessage());
    }

    // Clase para representar los errores de validación
    private static class DatosErrorValidacion {
        private final String campo;
        private final String mensaje;

        public DatosErrorValidacion(org.springframework.validation.FieldError error) {
            this.campo = error.getField();
            this.mensaje = error.getDefaultMessage();
        }

        public String getCampo() {
            return campo;
        }

        public String getMensaje() {
            return mensaje;
        }
    }
}
