package io.gianmarco.cleanArchitecture.domain.entities;

import java.time.Instant;
import java.util.UUID;

public class Otp {

    private UUID id;
    private UUID userId;
    private String email;
    private String otpHash;
    private OtpType type;
    private int attempts;
    private Instant expiresAt;

    private Otp() {}

    public static Otp create(
        UUID userId,
        String email,
        String otpHash,
        OtpType type,
        Instant expiresAt
    ) {
        Otp otp = new Otp();
        otp.userId = userId;
        otp.email = email;
        otp.otpHash = otpHash;
        otp.type = type;
        otp.expiresAt = expiresAt;
        otp.attempts = 0;
        return otp;
    }

    public static Otp restore(
        UUID id,
        UUID userId,
        String email,
        String otpHash,
        OtpType type,
        int attempts,
        Instant expiresAt
    ) {
        Otp otp = new Otp();
        otp.id = id;
        otp.userId = userId;
        otp.email = email;
        otp.otpHash = otpHash;
        otp.type = type;
        otp.attempts = attempts;
        otp.expiresAt = expiresAt;
        return otp;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public void increaseAttempts() {
        this.attempts++;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public OtpType getType() {
        return type;
    }

    public int getAttempts() {
        return attempts;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
