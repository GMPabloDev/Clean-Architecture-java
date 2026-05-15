package io.gianmarco.cleanArchitecture.application.ports.auth.resendOtp;

import io.gianmarco.cleanArchitecture.domain.entities.OtpType;

public record ResendOtpInput(String email, OtpType type) {}
