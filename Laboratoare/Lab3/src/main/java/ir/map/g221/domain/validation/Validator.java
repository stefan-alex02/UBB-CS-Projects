package ir.map.g221.domain.validation;

import ir.map.g221.exceptions.ValidationException;

public interface Validator<T> {
    /**
     *
     * @param entity
     * @throws ValidationException
     */
    void validate(T entity) throws ValidationException;
}
