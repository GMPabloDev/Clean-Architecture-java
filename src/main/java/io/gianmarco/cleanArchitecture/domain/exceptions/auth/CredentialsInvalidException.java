package io.gianmarco.cleanArchitecture.domain.exceptions.auth;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class CredentialsInvalidException extends DomainException {

    public CredentialsInvalidException() {
        super(
            "Invalid password for email ${dto.email}",
            "Correo electrónico o contraseña incorrectos",
            ErrorType.UNAUTHORIZED
        );
    }
}
