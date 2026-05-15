package io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.verifyEmail.VerifyEmailInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.verifyEmail.VerifyEmailOutput;

public interface VerifyEmailUseCase {
    VerifyEmailOutput execute(VerifyEmailInput input);
}
