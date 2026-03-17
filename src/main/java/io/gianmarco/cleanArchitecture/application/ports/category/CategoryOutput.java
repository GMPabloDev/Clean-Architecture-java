package io.gianmarco.cleanArchitecture.application.ports.category;

public record CategoryOutput(
        Long id,
        String name,
        String description,
        boolean active) {
}