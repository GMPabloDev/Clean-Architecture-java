package io.gianmarco.cleanArchitecture.infrastructure.persistence.repositories.user;

import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.domain.repositories.user.UserRepository;
import io.gianmarco.cleanArchitecture.infrastructure.mappers.user.UserMapper;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.user.UserJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.user.UserJpaEntity;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepositoryAdapter jpaRepository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
