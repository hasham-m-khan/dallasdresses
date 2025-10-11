package com.dallasdresses.models.response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private HttpStatus status;
    private String error;
    private HttpServletRequest request;
    private List<String> errors;
    private boolean success;

    public ResponseEntity<ApiResponse<Object>> createResponse() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("timestamp",  LocalDateTime.now());
        metadata.put("status", status.value());
        metadata.put("error", error);
        metadata.put("path",  request.getRequestURI());

        ApiResponse<Object> response = ApiResponse.builder()
                .success(success)
                .message(message)
                .errors(errors)
                .metadata(metadata)
                .build();

        return new ResponseEntity<>(response, status);
    }
}
