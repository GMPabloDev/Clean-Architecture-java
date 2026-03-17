package io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.otp;

import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.user.UserJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Data
@Table(name = "otps")
public class OtpJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String otp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @Column(length = 150)
    private String email;

    @Column(nullable = false)
    private Integer attempts = 0;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = false)
    private Instant expiresAt;
}
