package io.gianmarco.cleanArchitecture.application.useCases.impl.auth;

import io.gianmarco.cleanArchitecture.application.ports.auth.UserOutput;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.auth.GetCurrentUserUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.exceptions.ResourceNotFoundException;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import java.util.UUID;

public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    private final UserRepository userRepository;

    public GetCurrentUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserOutput execute(String userId) {
        UUID id = UUID.fromString(userId);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserOutput(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.isEmailVerified(),
            user.isDisabled()
        );
    }
}
