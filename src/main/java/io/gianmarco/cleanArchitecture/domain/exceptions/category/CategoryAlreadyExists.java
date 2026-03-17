package io.gianmarco.cleanArchitecture.domain.exceptions.category;

import io.gianmarco.cleanArchitecture.domain.exceptions.DomainException;
import io.gianmarco.cleanArchitecture.domain.exceptions.ErrorType;

public class CategoryAlreadyExists extends DomainException {

    public CategoryAlreadyExists(String name) {
        super(
                "Category with name " + name + " already exists",
                "Unable to create category. Please try again.",
                ErrorType.CONFLICT);
    }
}
