package com.example.gymsaas.common.security;

import com.example.gymsaas.common.api.ApiRes;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Turns security errors into the same JSON shape as ApiRes.
 * 401 = we don't know who you are. 403 = we know you, but you can't do this.
 */
@Component
public class SecurityErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public SecurityErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 401
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        String header = request.getHeader("Authorization");
        String message;

        if (header == null || !header.startsWith("Bearer ")) {
            message = "Authentication token is missing";
        } else if (ex instanceof DisabledException) {
            message = "Your account is not active";
        } else if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("expired")) {
            message = "Token has expired. Please log in again";
        } else {
            message = "Invalid token";
        }
        write(response, HttpServletResponse.SC_UNAUTHORIZED, message);
    }

    // 403
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        write(response, HttpServletResponse.SC_FORBIDDEN,
                "You do not have permission to perform this action");
    }

    private void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ApiRes.error(message));
    }
}