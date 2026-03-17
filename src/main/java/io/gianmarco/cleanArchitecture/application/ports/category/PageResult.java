package io.gianmarco.cleanArchitecture.application.ports.category;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}