package io.gianmarco.cleanArchitecture.infrastructure.persistence.repositories.refreshToken;

import io.gianmarco.cleanArchitecture.domain.entities.RefreshToken;
import io.gianmarco.cleanArchitecture.domain.repositories.refreshToken.RefreshTokenRepository;
import io.gianmarco.cleanArchitecture.infrastructure.mappers.refreshToken.RefreshTokenMapper;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.refreshToken.RefreshTokenJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.session.SessionJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.user.UserJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.refreshToken.RefreshTokenJpaEntity;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.session.SessionJpaEntity;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.user.UserJpaEntity;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RefreshTokenJpaRepositoryAdapter jpa;
    private final UserJpaRepositoryAdapter userJpa;
    private final SessionJpaRepositoryAdapter sessionJpa;
    private final RefreshTokenMapper mapper;

    @Override
    public RefreshToken save(RefreshToken token) {
        RefreshTokenJpaEntity entity = mapper.toJpa(token);

        UserJpaEntity userRef = userJpa.getReferenceById(token.getUserId());
        entity.setUser(userRef);

        if (token.getSessionId() != null) {
            SessionJpaEntity sessionRef = sessionJpa.getReferenceById(
                token.getSessionId()
            );
            entity.setSession(sessionRef);
        }

        RefreshTokenJpaEntity saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpa.findByToken(token).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(String token) {
        jpa.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteAllByUser(UUID userId) {
        jpa.deleteAllByUserId(userId);
    }
}
