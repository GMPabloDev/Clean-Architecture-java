package io.gianmarco.cleanArchitecture.application.useCases.impl.category;

import io.gianmarco.cleanArchitecture.application.ports.category.CategoryOutput;
import io.gianmarco.cleanArchitecture.application.ports.category.PageResult;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.ListCategoriesUseCase;
import io.gianmarco.cleanArchitecture.domain.repositories.category.CategoryRepository;

public class ListCategoriesUseCaseImpl implements ListCategoriesUseCase {

    private final CategoryRepository repository;

    public ListCategoriesUseCaseImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<CategoryOutput> execute(int page, int size) {
        var pageData = repository.findAll(page, size);

        var outputs = pageData
            .getContent()
            .stream()
            .map(c ->
                new CategoryOutput(
                    c.getId(),
                    c.getName(),
                    c.getDescription(),
                    c.isActive()
                )
            )
            .toList();

        return new PageResult<>(
            outputs,
            page,
            size,
            pageData.getTotalElements(),
            pageData.getTotalPages()
        );
    }
}
