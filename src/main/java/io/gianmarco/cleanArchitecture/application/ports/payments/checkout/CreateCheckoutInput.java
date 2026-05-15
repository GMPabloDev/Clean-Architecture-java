package io.gianmarco.cleanArchitecture.application.ports.payments.checkout;

public record CreateCheckoutInput(String priceId, String userId) {}
