package io.gianmarco.cleanArchitecture.infrastructure.config;

import io.gianmarco.cleanArchitecture.application.useCases.impl.category.CreateCategoryUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.impl.category.ListCategoriesUseCaseImpl;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.CreateCategoryUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.ListCategoriesUseCase;
import io.gianmarco.cleanArchitecture.domain.repositories.category.CategoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(
        CategoryRepository repository
    ) {
        return new CreateCategoryUseCaseImpl(repository);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase(
        CategoryRepository repository
    ) {
        return new ListCategoriesUseCaseImpl(repository);
    }
}
