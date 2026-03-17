package io.gianmarco.cleanArchitecture.infrastructure.mappers.category;

import io.gianmarco.cleanArchitecture.domain.entities.Category;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.category.CategoryJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryJpaEntity toJpa(Category category) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        if (category.getId() != null) {
            entity.setId(category.getId());
        }
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setActive(category.isActive());
        return entity;
    }

    public Category toDomain(CategoryJpaEntity entity) {
        return new Category(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.isActive()
        );
    }
}
