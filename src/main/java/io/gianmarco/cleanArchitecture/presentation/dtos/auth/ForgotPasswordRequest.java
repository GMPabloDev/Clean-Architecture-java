package io.gianmarco.cleanArchitecture.presentation.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
    @NotBlank(message = "email is required") @Email String email
) {}
