package ro.mpp2024.restclient;

import org.springframework.web.client.RestClientException;
import ro.mpp2024.domain.Configuration;
import ro.mpp2024.restclient.client.GameClient;
import ro.mpp2024.restclient.exceptions.ServiceException;

public class StartClient {
    private final static GameClient gameClient = new GameClient();

    static String alias = "a";
    static Configuration configuration = new Configuration(null, 3, 2, "Francezul sta in casa rosie");

    public static void main(String[] args) {
        try {
            System.out.println("Getting games by alias: ");

            show(()-> {
                var games = gameClient.getGamesOfPlayer(alias);
                games.forEach(System.out::println);
            });

            System.out.println("\nCreating a new configuration ...");
            show(() -> System.out.println(gameClient.addConfiguration(configuration)));

        } catch(RestClientException e) {
            System.out.println("Exception caught : " + e.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception"+ e);
        }
    }
}