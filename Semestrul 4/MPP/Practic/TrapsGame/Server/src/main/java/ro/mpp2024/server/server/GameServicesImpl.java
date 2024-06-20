package ro.mpp2024.server.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.*;
import ro.mpp2024.persistence.game.GameRepository;
import ro.mpp2024.persistence.user.UserRepository;
import ro.mpp2024.persistence.configuration.ConfigurationRepository;
import ro.mpp2024.services.GameException;
import ro.mpp2024.services.GameObserver;
import ro.mpp2024.services.GameServices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServicesImpl implements GameServices {
    private static final Logger logger = LogManager.getLogger(GameServicesImpl.class);

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ConfigurationRepository configurationRepository;

    private final Map<Integer, GameObserver> loggedClients;

    private final int defaultThreadsNo = 5;

    public GameServicesImpl(UserRepository userRepository,
                            GameRepository gameRepository,
                            ConfigurationRepository configurationRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.configurationRepository = configurationRepository;

        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized Game login(User user, GameObserver client) throws GameException {
        Optional<User> optionalUser = userRepository.findByAlias(user.getAlias());

        if (optionalUser.isEmpty()) {
            throw new GameException("User not found");
        }
        user = optionalUser.get();

        if (loggedClients.get(user.getId()) != null) {
            throw new GameException("User already logged in");
        }
        loggedClients.put(user.getId(), client);

        Collection<Configuration> holesConfiguration = new ArrayList<>();

        Random random = new Random();
        int numRows = 5;
        int numCols = 5;

        for (int col = 1; col <= numRows; col++) {
            int row = random.nextInt(numCols) + 1;

            Configuration.ConfigurationId configId = new Configuration.ConfigurationId(0, row, col);
            Configuration configuration = new Configuration(configId, null);
            holesConfiguration.add(configuration);
        }
        while (true) {
            int row = random.nextInt(numRows) + 1;
            int col = random.nextInt(numCols) + 1;

            if (holesConfiguration.stream().noneMatch(c -> c.getId().getRow() == row && c.getId().getColumn() == col)) {
                holesConfiguration.add(new Configuration(
                        new Configuration.ConfigurationId(0, row, col), null));
                break;
            }
        }

        return new Game(0, user, new ArrayList<>(), 0, 0, holesConfiguration, false);
    }

    @Override
    public synchronized void logout(User user, GameObserver client) throws GameException {
        GameObserver localClient = loggedClients.remove(user.getId());
        if (localClient == null)
            throw new GameException("User " + user.getId() + " is not logged in.");
    }

    @Override
    public Collection<Game> getGames() throws GameException {
        try {
            return gameRepository.getAll();
        }
        catch (Exception e) {
            throw new GameException("Error occurred", e);
        }
    }

    @Override
    public void saveGame(Game game) throws GameException {
        try {
            gameRepository.add(game);

            Collection<Game> games = gameRepository.getAll();

            notifyNewFinishedGame(games);
        }
        catch (Exception e) {
            throw new GameException("Error occurred", e);
        }
    }

    private void notifyNewFinishedGame(Collection<Game> games) throws GameException {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Map.Entry<Integer, GameObserver> entry : loggedClients.entrySet()) {
            executor.execute(() -> {
                try {
                    System.out.println("Notifying user [" + entry.getKey() + "]");
                    entry.getValue().newFinishedGame(games);
                } catch (GameException e) {
                    logger.error("Error notifying user", e);
                    System.err.println("Error notifying user " + e);
                }
            });
        }

        executor.shutdown();
    }

}
