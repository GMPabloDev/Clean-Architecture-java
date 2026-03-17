// domain/entities/Role.java
package io.gianmarco.cleanArchitecture.domain.entities;

import java.util.UUID;

public class Role {

    private final UUID id;
    private final String name; // "USER", "ADMIN", etc.

    private Role(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Role create(String name) {
        return new Role(UUID.randomUUID(), name);
    }

    public static Role withId(UUID id, String name) {
        return new Role(id, name);
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
