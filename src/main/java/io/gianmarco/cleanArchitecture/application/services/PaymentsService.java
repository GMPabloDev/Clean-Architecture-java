package io.gianmarco.cleanArchitecture.application.services;

public interface PaymentsService {
    String createCheckoutSession(String userEmail, String userId);
    String findOrCreateCustomer(String userEmail, String userId);
}
