package io.gianmarco.cleanArchitecture.infrastructure.persistence.repositories.category;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import io.gianmarco.cleanArchitecture.domain.entities.Category;
import io.gianmarco.cleanArchitecture.domain.entities.Page;
import io.gianmarco.cleanArchitecture.domain.repositories.category.CategoryRepository;
import io.gianmarco.cleanArchitecture.infrastructure.mappers.category.CategoryMapper;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.adapter.category.CategoryJpaRepositoryAdapter;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.category.CategoryJpaEntity;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

  private final CategoryJpaRepositoryAdapter categoryRepository;
  private final CategoryMapper mapper;

  public CategoryRepositoryImpl(
      CategoryJpaRepositoryAdapter jpaRepository,
      CategoryMapper mapper) {
    this.categoryRepository = jpaRepository;
    this.mapper = mapper;
  }

  @Override
  public Category save(Category category) {
    Objects.requireNonNull(category, "Category must not be null");
    CategoryJpaEntity entity = Objects.requireNonNull(
        mapper.toJpa(category),
        "Mapped entity must not be null");
    CategoryJpaEntity saved = categoryRepository.save(entity);
    return Objects.requireNonNull(
        mapper.toDomain(saved),
        "Mapped domain must not be null");
  }

  @Override
  public Optional<Category> findById(Long id) {
    Objects.requireNonNull(id, "id must not be null");
    return categoryRepository.findById(id)
        .map(mapper::toDomain);
  }

  @Override
  public boolean existsByName(String name) {
    return categoryRepository.existsByName(name);
  }

  @Override
  public Page<Category> findAll(int page, int size) {
    var pageable = PageRequest.of(page, size);
    var springPage = categoryRepository.findAll(pageable);

    var categories = springPage.getContent()
        .stream()
        .map(category -> mapper.toDomain(category))
        .toList();

    return new Page<Category>(
        categories,
        springPage.getTotalElements(),
        springPage.getTotalPages());
  }
}
