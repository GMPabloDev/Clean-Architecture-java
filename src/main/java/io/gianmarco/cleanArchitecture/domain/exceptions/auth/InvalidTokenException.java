// domain/exceptions/auth/InvalidTokenException.java
package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class InvalidTokenException extends DomainException {

    public InvalidTokenException() {
        super(
            "JWT token is invalid or malformed.",
            "Invalid authentication token.",
            ErrorType.UNAUTHORIZED
        );
    }
}
