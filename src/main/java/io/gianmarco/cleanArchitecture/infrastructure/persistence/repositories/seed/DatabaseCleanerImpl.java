package io.gianmarco.cleanArchitecture.infrastructure.persistence.repositories.seed;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DatabaseCleanerImpl implements DatabaseCleaner {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void clean() {
        entityManager
            .createNativeQuery("SET FOREIGN_KEY_CHECKS = 0")
            .executeUpdate();

        entityManager
            .createNativeQuery("TRUNCATE TABLE category")
            .executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE users").executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE otps").executeUpdate();

        entityManager
            .createNativeQuery("TRUNCATE TABLE sessions")
            .executeUpdate();
        entityManager
            .createNativeQuery("TRUNCATE TABLE refresh_tokens")
            .executeUpdate();

        entityManager
            .createNativeQuery("SET FOREIGN_KEY_CHECKS = 1")
            .executeUpdate();
    }
}
