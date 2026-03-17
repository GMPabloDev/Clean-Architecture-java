package io.gianmarco.cleanArchitecture.infrastructure.mappers.otp;

import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OtpMapper {

    public OtpJpaEntity toJpa(Otp otp) {
        OtpJpaEntity entity = new OtpJpaEntity();
        entity.setId(otp.getId());
        entity.setOtp(otp.getOtpHash());
        entity.setType(mapToJpaType(otp.getType()));
        entity.setEmail(otp.getEmail());
        entity.setAttempts(otp.getAttempts());
        entity.setExpiresAt(otp.getExpiresAt());
        return entity;
    }

    public Otp toDomain(OtpJpaEntity entity) {
        return Otp.restore(
            entity.getId(),
            entity.getUser() != null ? entity.getUser().getId() : null,
            entity.getEmail(),
            entity.getOtp(),
            mapToDomainType(entity.getType()),
            entity.getAttempts(),
            entity.getExpiresAt()
        );
    }

    private io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpType mapToJpaType(
        OtpType type
    ) {
        return io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpType.valueOf(
            type.name()
        );
    }

    private OtpType mapToDomainType(
        io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpType type
    ) {
        return OtpType.valueOf(type.name());
    }
}
