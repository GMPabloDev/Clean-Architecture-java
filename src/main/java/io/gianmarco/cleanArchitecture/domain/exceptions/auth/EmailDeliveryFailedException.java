package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class EmailDeliveryFailedException extends DomainException {

    public EmailDeliveryFailedException() {
        super(
            "Failed to send validation email.",
            "We encountered an issue sending the verification email. Please try again.",
            ErrorType.INTERNAL
        );
    }
}
