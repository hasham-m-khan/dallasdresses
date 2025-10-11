package com.dallasdresses.advices;

import com.dallasdresses.exceptions.users.DuplicateUserException;
import com.dallasdresses.exceptions.users.UserCreationException;
import com.dallasdresses.exceptions.users.UserNotFoundException;
import com.dallasdresses.models.response.ApiResponse;
import com.dallasdresses.models.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request) {

        log.error("🥊 User not found: " + ex.getMessage());

        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                "user not found",
                request,
                null,
                false)
                .createResponse();
    }

    @ExceptionHandler({UserCreationException.class})
    public ResponseEntity<ApiResponse<Object>> handleUserCreationException(
            UserCreationException ex,
            HttpServletRequest request) {

        log.error("🥊 Error creating user: " + ex.getMessage());

        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error creating User. Please ensure the object has all required fields.",
                request,
                null,
                false)
                .createResponse();
    }

    @ExceptionHandler({DuplicateUserException.class})
    public ResponseEntity<ApiResponse<Object>> handleDuplicateUserException(
            DuplicateUserException ex,
            HttpServletRequest request) {

        log.error("🥊 Error creating user: " + ex.getMessage());

        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error creating User. User with the updated email already exists.",
                request,
                null,
                false)
                .createResponse();
    }
}
