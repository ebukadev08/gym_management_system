package com.example.gymsaas.common.exception;

import com.example.gymsaas.common.api.ApiRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errors we throw ourselves (not found, conflict, and so on)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiRes<Void>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiRes.error(ex.getMessage()));
    }

    // 403: logged in, but not allowed (@PreAuthorize failed)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiRes<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiRes.error("You do not have permission to perform this action"));
    }

    // 400: the request body failed validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiRes<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(ApiRes.error("Validation failed", errors));
    }

    // 500: anything unexpected
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiRes<Void>> handleUnexpected(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiRes.error("Something went wrong. Please try again later"));
    }
}