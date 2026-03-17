package io.gianmarco.cleanArchitecture.presentation.dtos.category;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        boolean active) {
}