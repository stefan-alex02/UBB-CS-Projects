package ir.map.g221.domain.validation;

import ir.map.g221.exceptions.ValidationException;

public interface Validator<T> {
    /**
     *
     * @param entity The entity that is validated.
     * @throws ValidationException if the entity has invalid attributes.
     */
    void validate(T entity) throws ValidationException;
}
