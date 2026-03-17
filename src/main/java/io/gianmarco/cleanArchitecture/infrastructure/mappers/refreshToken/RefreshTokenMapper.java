package io.gianmarco.cleanArchitecture.infrastructure.mappers.refreshToken;

import io.gianmarco.cleanArchitecture.domain.entities.RefreshToken;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.refreshToken.RefreshTokenJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshTokenJpaEntity toJpa(RefreshToken token) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(token.getId());
        entity.setToken(token.getToken());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setRevokedAt(token.getRevokedAt());
        return entity;
    }

    public RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        return RefreshToken.restore(
            entity.getId(),
            entity.getUser().getId(),
            entity.getSession() != null ? entity.getSession().getId() : null,
            entity.getToken(),
            entity.getExpiresAt(),
            entity.getRevokedAt()
        );
    }
}
