package io.gianmarco.cleanArchitecture.presentation.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(

        @NotBlank(message = "Name is required") @Size(min = 3, max = 50) String name,

        @NotBlank @Size(min = 5, max = 200) String description) {
}
