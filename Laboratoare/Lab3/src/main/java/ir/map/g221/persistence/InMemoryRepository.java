package ir.map.g221.persistence;

import ir.map.g221.domain.Entity;
import ir.map.g221.domain.validation.Validator;
import ir.map.g221.exceptions.ValidationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Map<ID, E> entities;
    private final Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E add(E entity) throws IllegalArgumentException, ValidationException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null.");
        }
        validator.validate(entity);
        if (getById(entity.getId()) != null) {
            return null;
        }
        entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public E getById(ID id) throws IllegalArgumentException {
        testForNullId(id);
        return entities.get(id);
    }

    @Override
    public Collection<E> getAll() {
        return entities.values();
    }

    @Override
    public int getSize() {
        return entities.size();
    }

    @Override
    public E update(E entity) throws IllegalArgumentException, ValidationException {
        return null;
    }

    @Override
    public E delete(ID id) throws IllegalArgumentException {
        testForNullId(id);
        E entity = getById(id);

        if (entity == null) {
            return null;
        }
        entities.remove(id);
        return entity;
    }

    private static <ID> void testForNullId(ID id) throws IllegalArgumentException{
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null.");
        }
    }
}
