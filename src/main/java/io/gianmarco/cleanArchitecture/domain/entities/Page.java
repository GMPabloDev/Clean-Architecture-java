package io.gianmarco.cleanArchitecture.domain.entities;

import java.util.List;

public class Page<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;

    public Page(List<T> content, long totalElements, int totalPages) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
