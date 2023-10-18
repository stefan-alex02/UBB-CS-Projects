package ir.map.g221.ui;

import ir.map.g221.business.UserService;
import ir.map.g221.exceptions.ValidationException;
import ir.map.g221.factory.SampleGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UserConsole implements UserInterface{
    private final UserService userService;
    private final SampleGenerator sampleGenerator;

    public UserConsole(UserService userService, SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.sampleGenerator = sampleGenerator;
    }

    private void displayCommunities() {
        var communities = userService.calculateCommunities();
        if (communities.isEmpty()) {
            System.out.println("There are no users in the network.\n");
        }
        else {
            System.out.println("There are " + communities.size() + " communities:\n");
            userService.calculateCommunities().forEach(System.out::println);
        }
    }

    private void generateSample() {
        sampleGenerator.generateSample();
        System.out.println("Sample generated successfully.\n");
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        while(true) {
            System.out.println("========================");
            System.out.println("> Options:\n");
            System.out.println("0 - Exit program.");
            System.out.println("1 - Generate sample.");
            System.out.println("5 - Display all communities.");
            System.out.println("========================");

            try {
                String input = reader.readLine();
                switch(Integer.valueOf(input)) {
                    case 0:
                        System.out.println("Program closing...");
                        return;
                    case 1:
                        generateSample();
                        break;
                    case 5:
                        displayCommunities();
                        break;
                    default:
                        System.out.println("Unknown command");

                }
            }
            catch(ValidationException ve) {
                System.out.println("Validation Error:\n" + ve.getMessage());
            }
            catch(Exception e) {
                System.out.println("There has been an error:\n" + e.getMessage());
            }
        }
    }
}
