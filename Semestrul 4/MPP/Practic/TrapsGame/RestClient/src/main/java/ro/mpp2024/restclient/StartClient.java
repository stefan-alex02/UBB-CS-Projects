package ro.mpp2024.restclient;

import ro.mpp2024.domain.Configuration;
import ro.mpp2024.restclient.client.GameClient;
import ro.mpp2024.restclient.exceptions.ServiceException;


public class StartClient {
    private final static GameClient gameClient = new GameClient();

    static String alias = "a";
    static Configuration configuration = new Configuration(
            new Configuration.ConfigurationId(null, 3, 2), null);

    public static void main(String[] args) {
        try {
            configuration.setSymbol('E');

            System.out.println("\nUpdating configuration ...");
            show(() -> System.out.println(gameClient.updateConfiguration(configuration)));

        } catch(ServiceException e) {
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