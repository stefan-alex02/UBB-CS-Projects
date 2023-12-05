package ir.map.g221.guisocialnetwork;

import ir.map.g221.guisocialnetwork.factory.Factory;

import java.sql.SQLException;

public class OldMain {
    public static void main(String[] args) {
        try {
            var container = Factory.getInstance().build();
            container.getUi().run();

            container.getDatabaseConnection().closeConnection();
            System.out.println("SQL Connection closed successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}