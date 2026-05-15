package io.gianmarco.cleanArchitecture.infrastructure.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripePayments {

    @Value("${stripe.api-key}")
    private String apiKey;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    public String createCheckoutSession(
        String userEmail,
        String userId,
        String priceId
    ) throws StripeException {
        Stripe.apiKey = apiKey;

        // Buscar o crear el Customer en Stripe
        String customerId = findOrCreateCustomer(userEmail, userId);

        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
            .setCustomer(customerId)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            // Datos del usuario para recuperarlos en el webhook
            .putMetadata("userId", userId)
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPrice(priceId)
                    .setQuantity(1L)
                    .build()
            )
            // Prefill del email en el form de Stripe
            .setCustomerEmail(customerId == null ? userEmail : null)
            .build();

        Session session = Session.create(params);
        return session.getUrl();
    }

    private String findOrCreateCustomer(String email, String userId)
        throws StripeException {
        // Buscar si ya existe un customer con este email
        CustomerListParams listParams = CustomerListParams.builder()
            .setEmail(email)
            .setLimit(1L)
            .build();

        CustomerCollection customers = Customer.list(listParams);

        if (!customers.getData().isEmpty()) {
            return customers.getData().get(0).getId();
        }

        // Crear nuevo customer
        CustomerCreateParams createParams = CustomerCreateParams.builder()
            .setEmail(email)
            .putMetadata("userId", userId)
            .build();

        return Customer.create(createParams).getId();
    }
}
