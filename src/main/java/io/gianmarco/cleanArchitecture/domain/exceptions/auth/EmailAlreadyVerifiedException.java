package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class EmailAlreadyVerifiedException extends DomainException {

    public EmailAlreadyVerifiedException() {
        super(
            "Email already verified.",
            "Este correo ya ha sido verificado.",
            ErrorType.VALIDATION
        );
    }
}
