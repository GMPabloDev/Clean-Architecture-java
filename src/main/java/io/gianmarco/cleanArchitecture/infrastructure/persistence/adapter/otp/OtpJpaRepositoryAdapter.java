package io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.otp;

import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpJpaEntity;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp.OtpType;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OtpJpaRepositoryAdapter
    extends JpaRepository<OtpJpaEntity, UUID>
{
    Optional<OtpJpaEntity> findTopByEmailAndTypeOrderByCreatedAtDesc(
        String email,
        OtpType type
    );

    @Modifying
    @Query(
        "DELETE FROM OtpJpaEntity o WHERE o.email = :email AND o.type = :type"
    )
    void deleteByEmailAndType(
        @Param("email") String email,
        @Param("type") OtpType type
    );

    // Para deleteExpired
    @Modifying
    @Query("DELETE FROM OtpJpaEntity o WHERE o.expiresAt < :now")
    int deleteByExpiresAtBefore(@Param("now") Instant now);
}
