package org.example.zboruri.validation;

import org.example.zboruri.domain.Ticket;
import org.example.zboruri.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Objects;

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
    public void validate(Ticket ticket) throws ValidationException {
        String errors = "";

        if (ticket.getID().getValue() < 0) {
            errors += "Ticket flight ID is invalid";
        }

        if (ticket.getPurchaseTime().isBefore(LocalDateTime.of(1900, 1, 1, 0, 0))) {
            errors += "Purchase time is invalid";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
