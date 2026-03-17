package io.gianmarco.cleanArchitecture.application.ports.category;

public record CreateCategoryInput(
        String name,
        String description) {
}