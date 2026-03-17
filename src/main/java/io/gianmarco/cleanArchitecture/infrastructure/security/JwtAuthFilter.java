// infrastructure/security/JwtAuthFilter.java
package io.gianmarco.cleanArchitecture.infrastructure.security;

import io.gianmarco.cleanArchitecture.application.services.TokenManager;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.InvalidTokenException;
import io.gianmarco.cleanArchitecture.domain.exceptions.auth.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;
    private final JwtAuthEntryPoint authEntryPoint;

    public JwtAuthFilter(
        TokenManager tokenManager,
        UserDetailsService userDetailsService,
        JwtAuthEntryPoint authEntryPoint
    ) {
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Sin header → continúa (Spring Security decidirá si la ruta es pública o no)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            UUID userId = tokenManager.validateAccessToken(token);

            // Solo autentica si no hay autenticación previa en el contexto
            if (
                SecurityContextHolder.getContext().getAuthentication() == null
            ) {
                var userDetails = userDetailsService.loadUserByUsername(
                    userId.toString()
                );

                var authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (TokenExpiredException | InvalidTokenException ex) {
            // Delega al entry point para devolver JSON consistente con tu ErrorResponse
            authEntryPoint.commence(
                request,
                response,
                new org.springframework.security.core.AuthenticationException(
                    ex.getPublicMessage()
                ) {}
            );
        }
    }
}
