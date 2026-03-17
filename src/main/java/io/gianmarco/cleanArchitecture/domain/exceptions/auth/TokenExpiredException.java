// domain/exceptions/auth/TokenExpiredException.java
package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class TokenExpiredException extends DomainException {

    public TokenExpiredException() {
        super(
            "JWT token has expired.",
            "Your session has expired. Please log in again.",
            ErrorType.UNAUTHORIZED
        );
    }
}
