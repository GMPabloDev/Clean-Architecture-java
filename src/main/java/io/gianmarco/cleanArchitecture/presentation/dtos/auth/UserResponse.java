package io.gianmarco.cleanArchitecture.presentation.dtos.auth;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    boolean emailVerified,
    boolean disabled,
    Set<String> roles,
    Instant createdAt
) {}
