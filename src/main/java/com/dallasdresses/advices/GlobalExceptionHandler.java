package com.dallasdresses.advices;

import com.dallasdresses.exceptions.DuplicateEntityException;
import com.dallasdresses.exceptions.EntityNotFoundException;
import com.dallasdresses.exceptions.InvalidEntityException;
import com.dallasdresses.dtos.common.ApiResponse;
import com.dallasdresses.dtos.common.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Validation errors (from @Valid)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.error("🥊 Validation error: {}", ex.getMessage());

        List<String> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ErrorResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields",
                request,
                validationErrors,
                false)
                .createResponse();
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.error("🥊 Invalid request data: {}", ex.getMessage());

        String message = "Invalid request";
        List<String> errors = new ArrayList<>();

        // Check if it's an enum parsing error
        Throwable cause = ex.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            com.fasterxml.jackson.databind.exc.InvalidFormatException ife =
                    (com.fasterxml.jackson.databind.exc.InvalidFormatException) cause;

            if (ife.getTargetType().isEnum()) {
                message = "Invalid value for field '" + ife.getPath().get(0).getFieldName() + "'";
                errors.add(message);
            }
        }

        return new ErrorResponse(
                message,
                HttpStatus.BAD_REQUEST,
                "Invalid request",
                request,
                errors.isEmpty() ? null : errors,
                false)
                .createResponse();
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        log.error("🥊 Entity not found: {}", ex.getMessage());

        String message = "Entity not found";
        List<String> errors = new ArrayList<>();

        return new ErrorResponse(
                message,
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request,
                null,
                false)
                .createResponse();
    }

    @ExceptionHandler({DuplicateEntityException.class})
    public ResponseEntity<ApiResponse<Object>> handleDuplicateEntityException(
            DuplicateEntityException ex,
            HttpServletRequest request
    ) {
        log.error("🥊 Duplicate entity: {}", ex.getMessage());

        String message = "Duplicate entity";
        List<String> errors = new ArrayList<>();

        return new ErrorResponse(
                message,
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request,
                null,
                false)
                .createResponse();
    }

    @ExceptionHandler({InvalidEntityException.class})
    public ResponseEntity<ApiResponse<Object>> handleInvalidEntityException(
            EntityNotFoundException ex,
            HttpServletRequest request
    ) {
        log.error("🥊 Invalid entity: {}", ex.getMessage());

        String message = "Invalid entity";
        List<String> errors = new ArrayList<>();

        return new ErrorResponse(
                message,
                HttpStatus.BAD_REQUEST,
                "Invalid entity",
                request,
                null,
                false)
                .createResponse();
    }
}
