package org.example.Infrastructure.controller;

import org.example.Infrastructure.grpc.AvailableCarNotFoundException;
import org.example.Infrastructure.grpc.StorageGrpcUnavailableException;
import org.example.exception.BaseException;
import org.example.exception.DomainValidationExceptions.DomainValidationException;
import org.example.exception.NotFoundException.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse("NOT_FOUND", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(AvailableCarNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAvailableCarNotFound(AvailableCarNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse("NOT_FOUND", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(StorageGrpcUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handleStorageGrpcUnavailable(StorageGrpcUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiErrorResponse("STORAGE_SERVICE_UNAVAILABLE", exception.getMessage(), List.of()));
    }

    @ExceptionHandler({DomainValidationException.class, IllegalArgumentException.class, ClassCastException.class})
    public ResponseEntity<ApiErrorResponse> handleBadRequest(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse("BAD_REQUEST", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseException(BaseException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse("DOMAIN_ERROR", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse("VALIDATION_ERROR", "Request validation failed", details));
    }

    public record ApiErrorResponse(String code, String message, List<String> details) {
    }
}
