package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class OtpCooldownException extends DomainException {

    public OtpCooldownException(long seconds) {
        super(
            "Please wait " + seconds + " seconds before requesting a new code.",
            "Espere " +
                seconds +
                " segundos antes de solicitar un nuevo código.",
            ErrorType.VALIDATION
        );
    }
}
