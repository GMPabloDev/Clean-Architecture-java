package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class OtpExpiredException extends DomainException {

    public OtpExpiredException() {
        super(
            "OTP has expired.",
            "El código de verificación ha expirado.",
            ErrorType.VALIDATION
        );
    }
}
