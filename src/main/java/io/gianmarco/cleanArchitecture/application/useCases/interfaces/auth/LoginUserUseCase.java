package io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.login.LoginUserInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.login.LoginUserOutput;

public interface LoginUserUseCase {
    LoginUserOutput execute(LoginUserInput input);
}
