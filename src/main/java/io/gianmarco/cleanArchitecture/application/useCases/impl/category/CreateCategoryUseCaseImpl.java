package io.gianmarco.cleanArchitecture.application.useCases.impl.category;

import io.gianmarco.cleanArchitecture.application.ports.category.CategoryOutput;
import io.gianmarco.cleanArchitecture.application.ports.category.CreateCategoryInput;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.CreateCategoryUseCase;
import io.gianmarco.cleanArchitecture.domain.entities.Category;
import io.gianmarco.cleanArchitecture.domain.exceptions.category.CategoryAlreadyExists;
import io.gianmarco.cleanArchitecture.domain.repositories.category.CategoryRepository;;

public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {

    private final CategoryRepository repository;

    public CreateCategoryUseCaseImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryOutput execute(CreateCategoryInput input) {
        if (repository.existsByName(input.name())) {
            throw new CategoryAlreadyExists("Category already exists");
        }

        Category category = Category.create(
                input.name(),
                input.description());

        Category saved = repository.save(category);

        return new CategoryOutput(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.isActive());
    }

}
