package io.gianmarco.cleanArchitecture.application.services;

import io.gianmarco.cleanArchitecture.domain.entities.User;
import java.util.UUID;

public interface TokenManager {
    String generateAccessToken(User user);
    String generateRefreshToken(UUID userId);
    UUID validateAccessToken(String token); // retorna userId o lanza excepción
    UUID validateRefreshToken(String token); // retorna userId o lanza excepción
}
