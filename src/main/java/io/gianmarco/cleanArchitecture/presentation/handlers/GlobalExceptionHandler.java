package io.gianmarco.cleanArchitecture.presentation.handlers;

import io.gianmarco.cleanArchitecture.application.exceptions.ResourceNotFoundException;
import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.presentation.dtos.ErrorResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex
    ) {
        var errors = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .toList();

        var response = new ErrorResponse(
            LocalDateTime.now(),
            400,
            "Validation Error",
            "Invalid request body",
            errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(
        HttpMessageNotReadableException ex
    ) {
        var response = new ErrorResponse(
            LocalDateTime.now(),
            400,
            "Malformed JSON",
            "Request body is invalid or missing fields",
            List.of(ex.getMostSpecificCause().getMessage())
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomain(DomainException ex) {
        HttpStatus status = switch (ex.getType()) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case CONFLICT -> HttpStatus.CONFLICT;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        var response = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            ex.getType().name(),
            ex.getPublicMessage(),
            List.of()
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
        ResourceNotFoundException ex
    ) {
        var response = new ErrorResponse(
            LocalDateTime.now(),
            404,
            "Resource Not Found",
            ex.getMessage(),
            List.of()
        );

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        var response = new ErrorResponse(
            LocalDateTime.now(),
            500,
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred.",
            List.of()
        );
        return ResponseEntity.status(500).body(response);
    }
}
