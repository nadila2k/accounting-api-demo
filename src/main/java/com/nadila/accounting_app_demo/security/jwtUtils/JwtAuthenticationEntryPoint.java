package com.nadila.accounting_app_demo.security.jwtUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorBody.put("error", "Unauthorized");
        errorBody.put("message", authException.getMessage());
        errorBody.put("path", request.getRequestURI());
        errorBody.put("timestamp", LocalDateTime.now().toString());

        objectMapper.writeValue(response.getOutputStream(), errorBody);
    }
}
