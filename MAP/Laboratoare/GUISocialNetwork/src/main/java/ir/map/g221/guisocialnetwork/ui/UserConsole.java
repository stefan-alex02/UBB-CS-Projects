package ir.map.g221.guisocialnetwork.ui;

import ir.map.g221.guisocialnetwork.business.*;
import ir.map.g221.guisocialnetwork.domain.entities.FriendRequest;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.exceptions.ValidationException;
import ir.map.g221.guisocialnetwork.factory.SampleGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserConsole implements UserInterface{
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final FriendRequestService friendRequestService;
    private final CommunityHandler communityHandler;

    private final SampleGenerator sampleGenerator;

    public UserConsole(UserService userService,
                       FriendshipService friendshipService,
                       MessageService messageService, FriendRequestService friendRequestService, CommunityHandler communityHandler,
                       SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
        this.communityHandler = communityHandler;
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
        var communities = communityHandler.calculateCommunities();
        if (communities.isEmpty()) {
            System.out.println("There are no users in the network.\n");
        }
        else {
            System.out.println("\nThere are " + communities.size() + " communities:");
            communities.forEach(System.out::println);
        }
    }

    private void displayMostSociableCommunity() {
        var community = communityHandler.mostSociableCommunity();
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

    private void getConversation() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type one id: ");
        Long id1 = Long.valueOf(reader.readLine());

        System.out.print("> Type another id: ");
        Long id2 = Long.valueOf(reader.readLine());

        System.out.print("Conversation:\n");
        messageService.getConversation(id1, id2).forEach(message -> {
            System.out.println("[ " + message.getDate() +
                    " ] From " + message.getFrom().getFirstName() + " " + message.getFrom().getLastName() +
                    " : '" + message.getMessage() + "'");
        });
    }

    private void sendMessage() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type sender id: ");
        Long senderId = Long.valueOf(reader.readLine());

        System.out.print("> Type message: ");
        String message = reader.readLine();

        List<Long> receiverIds = new ArrayList<>();
        Long receiverId;
        do {
            System.out.print("> Type a receiver id (or -1 to end): ");
            receiverId = Long.valueOf(reader.readLine());

            if (receiverId != -1) {
                receiverIds.add(receiverId);
            }
        }
        while(receiverId != -1);

        messageService.sendMessageNow(senderId, receiverIds, message);

        System.out.println("Message sent with success.\n");
    }

    private void displayConversationUsers() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type sender id: ");
        Long senderId = Long.valueOf(reader.readLine());

        Set<User> conversationUsers = messageService.getConversationUsers(senderId);
        if (conversationUsers.isEmpty()) {
            System.out.println("User has no conversation users.");
        }
        else {
            System.out.println("The list of conversation users:");
            conversationUsers.forEach(System.out::println);
        }

    }

    private void sendFriendRequest() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type sender id: ");
        Long senderId = Long.valueOf(reader.readLine());

        System.out.print("> Type receiver id: ");
        Long receiverId = Long.valueOf(reader.readLine());

        friendRequestService.sendFriendRequestNow(senderId, receiverId);

        System.out.println("Friend request sent with success.\n");
    }

    private void approveFriendRequest() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type friend request id: ");
        Long friendRequestId = Long.valueOf(reader.readLine());

        friendRequestService.approveFriendRequest(friendRequestId);

        System.out.println("Friend request approved with success.\n");
    }

    private void rejectFriendRequest() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type friend request id: ");
        Long friendRequestId = Long.valueOf(reader.readLine());

        friendRequestService.rejectFriendRequest(friendRequestId);

        System.out.println("Friend request rejected with success.\n");
    }

    private void displayPendingFriendRequests() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("> Type receiver id: ");
        Long receiverId = Long.valueOf(reader.readLine());

        Set<FriendRequest> friendRequests = friendRequestService.getPendingFriendRequests(receiverId);

        if (friendRequests.isEmpty()) {
            System.out.println("There are no pending friend requests.\n");
        }
        else {
            System.out.println("Pending friend requests:");
            friendRequests.forEach(System.out::println);
        }
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
            System.out.println("10 - Get conversation.");
            System.out.println("11 - Send message.");
            System.out.println("12 - Get conversation users.");
            System.out.println("13 - Send friend request.");
            System.out.println("14 - Approve friend request.");
            System.out.println("15 - Reject friend request.");
            System.out.println("16 - See pending friend requests.");
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
                    case 10:
                        getConversation();
                        break;
                    case 11:
                        sendMessage();
                        break;
                    case 12:
                        displayConversationUsers();
                        break;
                    case 13:
                        sendFriendRequest();
                        break;
                    case 14:
                        approveFriendRequest();
                        break;
                    case 15:
                        rejectFriendRequest();
                        break;
                    case 16:
                        displayPendingFriendRequests();
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
