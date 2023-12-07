package ir.map.g221.guisocialnetwork.domain.validation;

import ir.map.g221.guisocialnetwork.exceptions.ValidationException;

import java.util.List;

public interface Validator<T> {
    /**
     * Validates a given entity.
     * @param entity the entity that is validated
     * @throws ValidationException if the entity has invalid attributes
     */
    default void validate(T entity) throws ValidationException {
        throwErrorsIfAny(findErrors(entity));
    }

    /**
     * Searches for validation errors for given entity.
     * @param entity the entity that is validated
     * @return the list of strings of errors (if any)
     */
    List<String> findErrors(T entity);

    /**
     * Throws exception if there are any errors in given list.
     * @param errors the list of possible errors
     * @throws ValidationException if there are any errors
     */
    default void throwErrorsIfAny(List<String> errors) throws ValidationException {
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
