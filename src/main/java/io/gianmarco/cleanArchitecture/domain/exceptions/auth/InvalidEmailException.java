package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class InvalidEmailException extends DomainException {

    public InvalidEmailException() {
        super(
            "The provided email is invalid or empty.",
            "Please check the email format and try again.",
            ErrorType.VALIDATION
        );
    }
}
