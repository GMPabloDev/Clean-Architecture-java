package io.gianmarco.cleanArchitecture.domain.repositories.category;

import java.util.Optional;
import io.gianmarco.cleanArchitecture.domain.entities.Category;
import io.gianmarco.cleanArchitecture.domain.entities.Page;

public interface CategoryRepository {

  Category save(Category category);
  Optional<Category> findById(Long id);
  Page<Category> findAll(int page, int size);
  boolean existsByName(String name);
}