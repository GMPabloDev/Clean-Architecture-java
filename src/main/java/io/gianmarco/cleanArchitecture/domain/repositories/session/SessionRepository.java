package io.gianmarco.cleanArchitecture.domain.repositories.session;

import io.gianmarco.cleanArchitecture.domain.entities.Session;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository {
    Session save(Session session);
    Optional<Session> findById(UUID id);
}
