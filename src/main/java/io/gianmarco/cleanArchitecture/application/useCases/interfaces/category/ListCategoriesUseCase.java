package io.gianmarco.cleanArchitecture.application.useCases.interfaces.category;

import io.gianmarco.cleanArchitecture.application.ports.category.CategoryOutput;
import io.gianmarco.cleanArchitecture.application.ports.category.PageResult;

public interface ListCategoriesUseCase {

    PageResult<CategoryOutput> execute(int page, int size);
}