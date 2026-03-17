package io.gianmarco.cleanArchitecture.infrastructure.persistence.repositories.otp;

import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import io.gianmarco.cleanArchitecture.domain.repositories.otp.OtpRepository;
import io.gianmarco.cleanArchitecture.infrastructure.mappers.otp.OtpMapper;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.otp.OtpJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpJpaEntity;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class OtpRepositoryImpl implements OtpRepository {

    private final OtpJpaRepositoryAdapter jpaRepository;
    private final OtpMapper mapper;

    @Override
    public Otp save(Otp otp) {
        OtpJpaEntity entity = mapper.toJpa(otp);
        OtpJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Otp> findLatestByEmailAndType(String email, OtpType type) {
        var jpaType =
            io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpType.valueOf(
                type.name()
            );
        return jpaRepository
            .findTopByEmailAndTypeOrderByCreatedAtDesc(email, jpaType)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Otp> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteByOwner(String email, OtpType type) {
        var jpaType =
            io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpType.valueOf(
                type.name()
            );
        jpaRepository.deleteByEmailAndType(email, jpaType);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public int deleteExpired() {
        return jpaRepository.deleteByExpiresAtBefore(Instant.now());
    }
}
