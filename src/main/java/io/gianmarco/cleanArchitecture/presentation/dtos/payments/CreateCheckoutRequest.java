package io.gianmarco.cleanArchitecture.presentation.dtos.payments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCheckoutRequest(
    @NotBlank(message = "priceId es requerido") String priceId,

    @NotNull(message = "userId es requerido") String userId,

    // Opcional: metadata adicional
    String planName,
    String successUrl,
    String cancelUrl
) {}
