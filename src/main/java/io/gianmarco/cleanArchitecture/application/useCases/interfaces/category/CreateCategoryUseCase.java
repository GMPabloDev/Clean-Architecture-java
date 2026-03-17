package io.gianmarco.cleanArchitecture.application.useCases.interfaces.category;

import io.gianmarco.cleanArchitecture.application.ports.category.CategoryOutput;
import io.gianmarco.cleanArchitecture.application.ports.category.CreateCategoryInput;

public interface CreateCategoryUseCase {
    CategoryOutput execute(CreateCategoryInput input);
}
