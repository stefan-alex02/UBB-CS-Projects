package org.example.practic.validation;

import org.example.practic.domain.DummyEntity;
import org.example.practic.exceptions.ValidationException;

public class DummyEntityValidator implements Validator<DummyEntity> {
    private static DummyEntityValidator instance;

    private DummyEntityValidator() {
    }

    public static DummyEntityValidator getInstance() {
        if (instance == null) {
            instance = new DummyEntityValidator();
        }

        return instance;
    }

    @Override
    public void validate(DummyEntity order) throws ValidationException {
        throw new UnsupportedOperationException();
    }
}
