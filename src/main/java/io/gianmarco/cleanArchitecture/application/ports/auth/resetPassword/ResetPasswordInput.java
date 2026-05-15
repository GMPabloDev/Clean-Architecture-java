package io.gianmarco.cleanArchitecture.application.ports.auth.resetPassword;

public record ResetPasswordInput(
    String email,
    String otp,
    String newPassword
) {}
