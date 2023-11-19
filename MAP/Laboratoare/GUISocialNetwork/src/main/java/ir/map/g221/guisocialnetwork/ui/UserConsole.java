package ir.map.g221.guisocialnetwork.ui;

import ir.map.g221.guisocialnetwork.business.FriendshipService;
import ir.map.g221.guisocialnetwork.business.UserService;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import ir.map.g221.guisocialnetwork.business.SampleGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;

public class UserConsole implements UserInterface{
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final SampleGenerator sampleGenerator;

    public UserConsole(UserService userService, FriendshipService friendshipService, SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.sampleGenerator = sampleGenerator;
    }

    private void addUser() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type first name: ");
        String firstName = reader.readLine();

        System.out.print("> Type last name: ");
        String lastName = reader.readLine();

        userService.addUser(firstName, lastName);

        System.out.println("User added successfully.\n");
    }

    private void removeUser() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type id: ");
        Long id = Long.valueOf(reader.readLine());

        userService.removeUser(id);

        System.out.println("User removed successfully.\n");
    }

    private void addFriendship() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type first id: ");
        Long id1 = Long.valueOf(reader.readLine());

        System.out.print("> Type second id: ");
        Long id2 = Long.valueOf(reader.readLine());

        friendshipService.addFriendshipNow(id1, id2);

        System.out.println("Friendship added successfully.\n");
    }

    private void removeFriendship() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type first id: ");
        Long id1 = Long.valueOf(reader.readLine());

        System.out.print("> Type second id: ");
        Long id2 = Long.valueOf(reader.readLine());

        friendshipService.removeFriendship(id1, id2);

        System.out.println("Friendship removed successfully.\n");
    }

    private void displayCommunities() {
        var communities = userService.calculateCommunities();
        if (communities.isEmpty()) {
            System.out.println("There are no users in the network.\n");
        }
        else {
            System.out.println("\nThere are " + communities.size() + " communities:");
            userService.calculateCommunities().forEach(System.out::println);
        }
    }

    private void displayMostSociableCommunity() {
        var community = userService.mostSociableCommunity();
        if (community.isEmpty()) {
            System.out.println("There are no users in the network.\n");
        }
        else {
            System.out.println("Most sociable community is:\n" + community +
                    "\nAnd the longest friendship path is:\n" + community.getFriendshipPath());
        }
    }

    private void displayFriendshipsOfUserInYearMonth() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type user id: ");
        Long id1 = Long.valueOf(reader.readLine());

        System.out.print("> Type year: ");
        int year = Integer.parseInt(reader.readLine());

        System.out.print("> Type month: ");
        int month = Integer.parseInt(reader.readLine());

        var friendshipDTOs = friendshipService.getFriendshipDetailsInYearMonth(id1, YearMonth.of(year, month));
        if (!friendshipDTOs.isEmpty()) {
            System.out.println("List of friendships :");
            friendshipDTOs.forEach(System.out::println);
        }
        else {
            System.out.println("User has no friendships in the given year and month.");
        }
    }

    private void getUserDetails() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type id: ");
        Long id = Long.valueOf(reader.readLine());

        System.out.print("User details: ");
        System.out.println(userService.getUser(id).toString());
    }

    private void generateSample() throws RuntimeException {
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
            System.out.println("2 - Add user.");
            System.out.println("3 - Remove user.");
            System.out.println("4 - Add friendship.");
            System.out.println("5 - Remove friendship.");
            System.out.println("6 - Display all communities.");
            System.out.println("7 - Show most sociable community.");
            System.out.println("8 - Show friendships of user.");
            System.out.println("9 - Get user details.");
            System.out.println("========================");

            try {
                String input = reader.readLine();
                switch(Integer.parseInt(input)) {
                    case 0:
                        System.out.println("Program closing...");
                        return;
                    case 1:
                        generateSample();
                        break;
                    case 2:
                        addUser();
                        break;
                    case 3:
                        removeUser();
                        break;
                    case 4:
                        addFriendship();
                        break;
                    case 5:
                        removeFriendship();
                        break;
                    case 6:
                        displayCommunities();
                        break;
                    case 7:
                        displayMostSociableCommunity();
                        break;
                    case 8:
                        displayFriendshipsOfUserInYearMonth();
                        break;
                    case 9:
                        getUserDetails();
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            }
            catch(ValidationException ve) {
                System.out.println("Validation Error:\n" + ve.getMessage() + "\n");
            }
            catch(Exception e) {
                System.out.println("There has been an error:\n" + e.getMessage() + "\n");
            }
        }
    }
}
