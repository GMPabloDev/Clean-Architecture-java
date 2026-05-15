package io.gianmarco.cleanArchitecture.application.useCases.impl.payments;

import io.gianmarco.cleanArchitecture.application.ports.payments.checkout.CreateCheckoutInput;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.payments.CreateCheckoutUseCase;

public class CreateCheckoutUseCaseImpl implements CreateCheckoutUseCase {

    @Override
    public String execute(CreateCheckoutInput input) {
        return "";
    }
}
