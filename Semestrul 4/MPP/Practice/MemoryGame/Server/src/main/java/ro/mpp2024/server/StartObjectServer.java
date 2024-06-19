package ro.mpp2024.server;

import ro.mpp2024.networking.utils.AbstractServer;
import ro.mpp2024.networking.utils.GameObjectConcurrentServer;
import ro.mpp2024.networking.utils.ServerException;
import ro.mpp2024.persistence.game.GameHibernateRepository;
import ro.mpp2024.persistence.game.GameRepository;
import ro.mpp2024.persistence.user.UserHibernateRepository;
import ro.mpp2024.persistence.user.UserRepository;
import ro.mpp2024.persistence.word.WordHibernateRepository;
import ro.mpp2024.persistence.word.WordRepository;
import ro.mpp2024.server.server.GameServicesImpl;
import ro.mpp2024.services.GameServices;

import java.io.IOException;
import java.util.Properties;


public class StartObjectServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
       Properties serverProps = new Properties();
        try {
            serverProps.load(StartObjectServer.class.getResourceAsStream("/gameserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find gameserver.properties "+e);
            return;
        }

        UserRepository userRepo = new UserHibernateRepository();
        GameRepository gameRepo = new GameHibernateRepository();
        WordRepository wordRepo = new WordHibernateRepository();

        GameServices chatServerImpl = new GameServicesImpl(userRepo, gameRepo, wordRepo);
        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("game.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new GameObjectConcurrentServer(chatServerPort, chatServerImpl);
        try {
                server.start();
        } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
