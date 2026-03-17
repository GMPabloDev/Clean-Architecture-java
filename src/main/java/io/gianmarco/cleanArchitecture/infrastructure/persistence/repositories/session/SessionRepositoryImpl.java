package io.gianmarco.cleanArchitecture.infrastructure.persistence.repositories.session;

import io.gianmarco.cleanArchitecture.domain.entities.Session;
import io.gianmarco.cleanArchitecture.domain.repositories.session.SessionRepository;
import io.gianmarco.cleanArchitecture.infrastructure.mappers.session.SessionMapper;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.session.SessionJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.user.UserJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.session.SessionJpaEntity;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.user.UserJpaEntity;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJpaRepositoryAdapter jpaRepository;
    private final UserJpaRepositoryAdapter userJpa;
    private final SessionMapper mapper;

    @Override
    public Session save(Session session) {
        SessionJpaEntity entity = mapper.toJpa(session);

        UserJpaEntity userRef = userJpa.getReferenceById(session.getUserId());
        entity.setUser(userRef);

        SessionJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Session> findById(UUID sessionId) {
        return jpaRepository.findById(sessionId).map(mapper::toDomain);
    }
}
