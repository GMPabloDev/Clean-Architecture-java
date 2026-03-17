package io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.register.RegisterUserInput;
import io.gianmarco.cleanArchitecture.application.ports.auth.register.RegisterUserOutput;

public interface CreateUserUseCase {
    RegisterUserOutput execute(RegisterUserInput input);
}
