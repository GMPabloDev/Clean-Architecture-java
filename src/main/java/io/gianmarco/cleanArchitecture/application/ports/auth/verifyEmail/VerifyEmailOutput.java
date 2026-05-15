package io.gianmarco.cleanArchitecture.application.ports.auth.verifyEmail;

public record VerifyEmailOutput(String accessToken, String refreshToken) {}
