package com.klearn.config;

import com.klearn.dto.ApiErrorResponse;
import com.klearn.exception.KLearnException;
import com.klearn.exception.ResourceNotFoundException;
import com.klearn.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Global exception handler for K-Learn API.
 * Handles all exceptions and returns standardized error responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
            .status("error")
            .code(404)
            .message(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .path(request.getDescription(false).replace("uri=", ""))
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            ValidationException ex,
            WebRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        
        ApiErrorResponse error = ApiErrorResponse.builder()
            .status("error")
            .code(400)
            .message(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .path(request.getDescription(false).replace("uri=", ""))
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Method Argument Validation Errors (DTO validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        log.warn("Validation failed for request: {}", ex.getMessage());
        
        var fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> ApiErrorResponse.FieldError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .rejectedValue(error.getRejectedValue())
                .build())
            .collect(Collectors.toList());

        ApiErrorResponse error = ApiErrorResponse.builder()
            .status("error")
            .code(400)
            .message("Validation failed")
            .errorCode("VALIDATION_ERROR")
            .path(request.getDescription(false).replace("uri=", ""))
            .timestamp(LocalDateTime.now())
            .fieldErrors(fieldErrors)
            .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all K-Learn specific exceptions
     */
    @ExceptionHandler(KLearnException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleKLearnException(
            KLearnException ex,
            WebRequest request) {
        log.error("K-Learn exception: {}", ex.getMessage(), ex);
        
        ApiErrorResponse error = ApiErrorResponse.builder()
            .status("error")
            .code(400)
            .message(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .path(request.getDescription(false).replace("uri=", ""))
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all other exceptions (catch-all)
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception ex,
            WebRequest request) {
        log.error("Unexpected error occurred", ex);
        
        ApiErrorResponse error = ApiErrorResponse.builder()
            .status("error")
            .code(500)
            .message("An unexpected error occurred")
            .errorCode("INTERNAL_SERVER_ERROR")
            .path(request.getDescription(false).replace("uri=", ""))
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
