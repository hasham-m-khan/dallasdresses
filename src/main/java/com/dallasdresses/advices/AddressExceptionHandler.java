package com.dallasdresses.advices;

import com.dallasdresses.exceptions.addresses.AddressNotFoundException;
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
public class AddressExceptionHandler {

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleAddressNotFoundException(
            AddressNotFoundException ex,
            HttpServletRequest request) {

        log.error("🥊 Error fetching addresses: {}", ex.getMessage());

        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error fetching address.",
                request,
                null,
                false)
                .createResponse();
    }
}
