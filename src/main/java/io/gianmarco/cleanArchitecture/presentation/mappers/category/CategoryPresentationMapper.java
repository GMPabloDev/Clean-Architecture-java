package io.gianmarco.cleanArchitecture.presentation.mappers.category;

import io.gianmarco.cleanArchitecture.application.ports.category.CategoryOutput;
import io.gianmarco.cleanArchitecture.presentation.dtos.category.CategoryResponse;

public class CategoryPresentationMapper {
    public static CategoryResponse toResponse(CategoryOutput output) {
        return new CategoryResponse(
                output.id(),
                output.name(),
                output.description(),
                output.active());
    }
}
