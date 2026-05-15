package io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.resetPassword.ResetPasswordInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.resetPassword.ResetPasswordOutput;

public interface ResetPasswordUseCase {
    ResetPasswordOutput execute(ResetPasswordInput input);
}
