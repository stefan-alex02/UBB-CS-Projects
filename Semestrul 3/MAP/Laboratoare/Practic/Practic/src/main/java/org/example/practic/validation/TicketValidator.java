package org.example.practic.validation;

import org.example.practic.domain.DummyEntity;
import org.example.practic.domain.Ticket;
import org.example.practic.exceptions.ValidationException;

public class TicketValidator implements Validator<Ticket> {
    private static TicketValidator instance;

    private TicketValidator() {
    }

    public static TicketValidator getInstance() {
        if (instance == null) {
            instance = new TicketValidator();
        }

        return instance;
    }

    @Override
    public void validate(Ticket order) throws ValidationException {
        return;
    }
}
