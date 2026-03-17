package io.gianmarco.cleanArchitecture.domain.entities;

public class Category {

    private Long id;
    private String name;
    private String description;
    private boolean active;

    public Category(Long id, String name, String description, boolean active) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public static Category create(String name, String description) {
        return new Category(null, name, description, true);
    }

    public void deactivate() {
        if (!active) {
            throw new IllegalStateException("Category already inactive");
        }
        this.active = false;
    }

    public void updateDescription(String newDescription) {
        if (newDescription == null || newDescription.length() < 5) {
            throw new IllegalArgumentException(
                "Description must have at least 5 characters"
            );
        }
        this.description = newDescription;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }
}
