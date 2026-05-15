package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class OtpMaxAttemptsExceededException extends DomainException {

    public OtpMaxAttemptsExceededException() {
        super(
            "Maximum OTP attempts exceeded.",
            "Se ha excedido el número máximo de intentos. Solicita un nuevo código.",
            ErrorType.VALIDATION
        );
    }
}
