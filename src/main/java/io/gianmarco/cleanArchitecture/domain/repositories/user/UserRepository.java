package io.gianmarco.cleanArchitecture.domain.repositories.user;

import io.gianmarco.cleanArchitecture.domain.entities.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    boolean existsByEmail(String email);
}
