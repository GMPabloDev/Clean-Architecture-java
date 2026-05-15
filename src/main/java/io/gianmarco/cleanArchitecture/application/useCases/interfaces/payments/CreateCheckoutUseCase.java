package io.gianmarco.cleanArchitecture.application.useCases.interfaces.payments;

import io.gianmarco.cleanArchitecture.application.ports.payments.checkout.CreateCheckoutInput;

public interface CreateCheckoutUseCase {
    String execute(CreateCheckoutInput input);
}
