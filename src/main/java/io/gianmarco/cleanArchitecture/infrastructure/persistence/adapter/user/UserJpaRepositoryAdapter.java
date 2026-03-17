package io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.user;

import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.user.UserJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepositoryAdapter
    extends JpaRepository<UserJpaEntity, UUID>
{
    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
