package io.gianmarco.cleanArchitecture.infrastructure.mappers.session;

import io.gianmarco.cleanArchitecture.domain.entities.Session;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.session.SessionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public SessionJpaEntity toJpa(Session session) {
        SessionJpaEntity entity = new SessionJpaEntity();
        entity.setId(session.getId());
        entity.setIsActive(session.isActive());
        entity.setLastSeenAt(session.getLastSeenAt());
        entity.setExpiresAt(session.getExpiresAt());
        return entity;
    }

    public Session toDomain(SessionJpaEntity entity) {
        return Session.restore(
            entity.getId(),
            entity.getUser().getId(),
            entity.getIsActive(),
            entity.getLastSeenAt(),
            entity.getExpiresAt()
        );
    }
}
