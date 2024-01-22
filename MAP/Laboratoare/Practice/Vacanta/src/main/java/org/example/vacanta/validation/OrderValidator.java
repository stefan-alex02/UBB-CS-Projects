package org.example.vacanta.validation;

import org.example.modelpractic.domain.Order;
import org.example.modelpractic.exceptions.ValidationException;

import java.util.Objects;

public class OrderValidator implements Validator<Order> {
    private static OrderValidator instance;

    private OrderValidator() {
    }

    public static OrderValidator getInstance() {
        if (instance == null) {
            instance = new OrderValidator();
        }

        return instance;
    }

    @Override
    public void validate(Order order) throws ValidationException {
        String errors = "";

        if (Objects.equals(order.getPerson().getID(), order.getTaxiDriver().getID())) {
            errors += "Order cannot be honored from yourself\n";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
