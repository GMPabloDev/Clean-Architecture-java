package io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.refreshToken;

import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.session.SessionJpaEntity;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.user.UserJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@Table(name = "refresh_tokens")
public class RefreshTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 500)
    private String token;

    @CreationTimestamp
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    private Instant revokedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private SessionJpaEntity session;
}
