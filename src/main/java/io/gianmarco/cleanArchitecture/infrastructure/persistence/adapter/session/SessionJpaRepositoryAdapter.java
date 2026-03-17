package io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.session;

import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.session.SessionJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionJpaRepositoryAdapter
    extends JpaRepository<SessionJpaEntity, UUID> {}
