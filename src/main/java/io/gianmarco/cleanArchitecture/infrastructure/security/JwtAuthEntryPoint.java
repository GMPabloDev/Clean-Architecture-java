// infrastructure/security/JwtAuthEntryPoint.java
package io.gianmarco.cleanArchitecture.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.gianmarco.cleanArchitecture.presentation.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public JwtAuthEntryPoint() {
        this.mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            401,
            "UNAUTHORIZED",
            authException.getMessage() != null
                ? authException.getMessage()
                : "Authentication required.",
            List.of()
        );

        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
