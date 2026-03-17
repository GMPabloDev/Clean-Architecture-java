// infrastructure/config/SecurityConfig.java
package io.gianmarco.cleanArchitecture.infrastructure.config;

import io.gianmarco.cleanArchitecture.infrastructure.security.JwtAccessDeniedHandler;
import io.gianmarco.cleanArchitecture.infrastructure.security.JwtAuthEntryPoint;
import io.gianmarco.cleanArchitecture.infrastructure.security.JwtAuthFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // habilita @PreAuthorize en controllers
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
        JwtAuthFilter jwtAuthFilter,
        JwtAuthEntryPoint authEntryPoint,
        JwtAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(ex ->
                ex
                    .authenticationEntryPoint(authEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .authorizeHttpRequests(auth ->
                auth
                    // Rutas públicas de auth
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/register")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/verify")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/login")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh")
                    .permitAll()
                    // Rutas PRIVADAS de auth (requieren token válido)
                    .requestMatchers(
                        HttpMethod.POST,
                        "/api/v1/auth/reset-password"
                    )
                    .authenticated()
                    // Agrega aquí más rutas privadas de auth según las necesites

                    // Todo lo demás requiere autenticación
                    .anyRequest()
                    .authenticated()
            )
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Ajusta estos orígenes a los de tus frontends
        //     config.setAllowedOriginPatterns(List.of("*"));

        config.setAllowedOriginPatterns(List.of("*"));

        //config.setAllowedOrigins(
        //    List.of(
        //        "http://localhost:3000", // Next.js dev
        //        "https://tu-dominio.com" // Next.js prod
        //        // Kotlin mobile no necesita CORS (hace requests directas HTTP)
        //    )
        //);
        config.setAllowedMethods(
            List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );
        config.setAllowedHeaders(
            List.of("Authorization", "Content-Type", "Accept")
        );
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
