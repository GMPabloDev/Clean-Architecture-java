package io.gianmarco.cleanArchitecture.application.ports.auth.verifyEmail;

public record VerifyEmailInput(String email, String otp) {}
