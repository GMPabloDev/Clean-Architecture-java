package io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.category;

import org.springframework.data.jpa.repository.JpaRepository;

import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.category.CategoryJpaEntity;

public interface CategoryJpaRepositoryAdapter extends JpaRepository<CategoryJpaEntity, Long> {
    boolean existsByName(String name);
}
