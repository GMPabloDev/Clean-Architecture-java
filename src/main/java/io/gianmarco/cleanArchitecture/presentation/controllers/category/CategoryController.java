package io.gianmarco.cleanArchitecture.presentation.controllers.category;

import io.gianmarco.cleanArchitecture.application.ports.category.CreateCategoryInput;
import io.gianmarco.cleanArchitecture.application.ports.category.PageResult;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.CreateCategoryUseCase;
import io.gianmarco.cleanArchitecture.application.useCases.interfaces.category.ListCategoriesUseCase;
import io.gianmarco.cleanArchitecture.presentation.dtos.category.CategoryResponse;
import io.gianmarco.cleanArchitecture.presentation.dtos.category.CreateCategoryRequest;
import io.gianmarco.cleanArchitecture.presentation.mappers.category.CategoryPresentationMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CreateCategoryUseCase createUseCase;
    private final ListCategoriesUseCase listUseCase;

    public CategoryController(
        CreateCategoryUseCase createUseCase,
        ListCategoriesUseCase listUseCase
    ) {
        this.createUseCase = createUseCase;
        this.listUseCase = listUseCase;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
        @Valid @RequestBody CreateCategoryRequest request
    ) {
        var input = new CreateCategoryInput(
            request.name(),
            request.description()
        );

        var output = createUseCase.execute(input);

        return ResponseEntity.status(201).body(
            CategoryPresentationMapper.toResponse(output)
        );
    }

    @GetMapping
    public ResponseEntity<PageResult<CategoryResponse>> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        var result = listUseCase.execute(page, size);

        var response = new PageResult<>(
            result
                .content()
                .stream()
                .map(CategoryPresentationMapper::toResponse)
                .toList(),
            result.page(),
            result.size(),
            result.totalElements(),
            result.totalPages()
        );

        return ResponseEntity.ok(response);
    }
}
