package io.gianmarco.cleanArchitecture.application.ports.auth.login;

public record LoginUserOutput(String accessToken, String refreshToken) {}
