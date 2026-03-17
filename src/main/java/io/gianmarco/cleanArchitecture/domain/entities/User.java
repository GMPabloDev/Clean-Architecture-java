package io.gianmarco.cleanArchitecture.domain.entities;

import io.gianmarco.cleanArchitecture.domain.exceptions.auth.InvalidEmailException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {

    private final UUID id;
    private final String email;
    private String name;
    private String password;
    private boolean emailVerified;
    private boolean disabled;
    private final Set<String> roles;
    private final Instant createdAt;
    private Instant updatedAt;

    private User(
        UUID id,
        String name,
        String email,
        String password,
        Instant createdAt
    ) {
        validateEmail(email);

        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.emailVerified = false;
        this.disabled = false;
        this.roles = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = Instant.now();
    }

    public static User create(
        String name,
        String email,
        String hashedPassword
    ) {
        return new User(null, name, email, hashedPassword, Instant.now());
    }

    public static User restore(
        UUID id,
        String name,
        String email,
        String password,
        boolean emailVerified,
        boolean disabled,
        Set<String> roles,
        Instant createdAt,
        Instant updatedAt
    ) {
        User user = new User(id, name, email, password, createdAt);
        user.emailVerified = emailVerified;
        user.disabled = disabled;
        user.roles.clear();
        user.roles.addAll(roles);
        user.updatedAt = updatedAt;
        return user;
    }

    public void verifyEmail() {
        this.emailVerified = true;
        touch();
    }

    public void disable() {
        this.disabled = true;
        touch();
    }

    public void changePassword(String hashedPassword) {
        this.password = hashedPassword;
        touch();
    }

    public void addRole(String role) {
        roles.add(role);
        touch();
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidEmailException();
        }
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
