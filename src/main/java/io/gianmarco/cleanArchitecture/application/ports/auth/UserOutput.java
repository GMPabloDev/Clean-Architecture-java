package io.gianmarco.cleanArchitecture.application.ports.auth;

import java.util.Set;
import java.util.UUID;

public record UserOutput(
    UUID id,
    String name,
    String email,
    boolean emailVerified,
    boolean disabled,
    Set<String> roles) {

}
