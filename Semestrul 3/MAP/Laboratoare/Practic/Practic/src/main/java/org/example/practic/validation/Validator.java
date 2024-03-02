package org.example.practic.validation;

import org.example.practic.exceptions.ValidationException;

public interface Validator<T> {
    public void validate(T entity) throws ValidationException;
}
