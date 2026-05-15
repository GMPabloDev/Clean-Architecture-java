package io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.forgotPassword.ForgotPasswordInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.forgotPassword.ForgotPasswordOutput;

public interface ForgotPasswordUseCase {
    ForgotPasswordOutput execute(ForgotPasswordInput input);
}
