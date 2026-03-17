package io.gianmarco.cleanArchitecture.domain.repositories.otp;

import io.gianmarco.cleanArchitecture.domain.entities.Otp;
import io.gianmarco.cleanArchitecture.domain.entities.OtpType;
import java.util.Optional;
import java.util.UUID;

public interface OtpRepository {
    Otp save(Otp otp);
    Optional<Otp> findLatestByEmailAndType(String email, OtpType type);
    Optional<Otp> findById(UUID id);
    void deleteByOwner(String email, OtpType type);
    void deleteById(UUID id);
    int deleteExpired();
}
