package io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.UserOutput;

public interface GetCurrentUserUseCase {
    UserOutput execute(String userId);
}
