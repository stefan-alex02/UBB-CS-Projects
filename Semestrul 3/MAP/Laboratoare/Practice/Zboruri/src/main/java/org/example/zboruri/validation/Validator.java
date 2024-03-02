package org.example.zboruri.validation;

import org.example.zboruri.exceptions.ValidationException;

public interface Validator<T> {
    public void validate(T entity) throws ValidationException;
}
