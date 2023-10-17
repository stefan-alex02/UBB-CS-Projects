package ir.map.g221.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UserConsole implements UserInterface{
    private static UserConsole instance = null;

    private UserConsole() {

    }

    public static UserConsole getInstance() {
        if (instance == null) {
            instance = new UserConsole();
        }

        return instance;
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        while(true) {
            System.out.println("========================");
            System.out.println("> Options:\n");
            System.out.println("0 - Exit program.");
            System.out.println("1 - ");
            System.out.println("========================");

            try {
                String input = reader.readLine();
                switch(Integer.valueOf(input)) {
                    case 0:
                        System.out.println("Program closing...");
                        return;
                    default:
                        System.out.println("Unknown command");

                }
            }
            catch(Exception e) {
                System.out.println("There has been an error.");
            }
        }
    }
}
