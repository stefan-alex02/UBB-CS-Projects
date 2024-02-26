package org.example.modelpractic.validation;

import org.example.modelpractic.exceptions.ValidationException;

public interface Validator<T> {
    public void validate(T entity) throws ValidationException;
}
