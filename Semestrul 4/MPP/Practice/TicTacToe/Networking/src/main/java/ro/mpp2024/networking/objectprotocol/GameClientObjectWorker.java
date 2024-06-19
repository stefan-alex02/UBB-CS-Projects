package ro.mpp2024.networking.objectprotocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.domain.Game;
import ro.mpp2024.domain.User;
import ro.mpp2024.networking.objectprotocol.dto.DTOUtils;
import ro.mpp2024.networking.objectprotocol.dto.GameDTO;
import ro.mpp2024.networking.objectprotocol.dto.UserDTO;
import ro.mpp2024.networking.objectprotocol.request.*;
import ro.mpp2024.networking.objectprotocol.response.*;
import ro.mpp2024.services.GameException;
import ro.mpp2024.services.GameObserver;
import ro.mpp2024.services.GameServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;

public class GameClientObjectWorker implements Runnable, GameObserver {
    private static final Logger logger = LogManager.getLogger(GameClientObjectWorker.class);

    private GameServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public GameClientObjectWorker(GameServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request)request);
                if (response != null){
                   sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    @Override
    public void newFinishedGame(Collection<Game> games) throws GameException {
        GameDTO[] dtos = DTOUtils.getDTO(games);

        System.out.println("New games received " + Arrays.toString(dtos));
        try {
            sendResponse(new NewFinishedGameResponse(dtos));
        } catch (IOException e) {
            throw new GameException("Sending error: " + e);
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    private Response handleRequest(Request request) {
        switch (request) {
            case LoginRequest loginRequest -> {
                System.out.println("Login request ...");

                UserDTO userDTO = loginRequest.user();
                User user = DTOUtils.getFromDTO(userDTO);

                try {
                    user = server.login(user, this);
                    UserDTO userDTO1 = DTOUtils.getDTO(user);
                    return new LoginResponse(userDTO1);
                }
                catch (GameException e) {
                    connected = false;
                    logger.error("Error while processing request", e);
                    return new ErrorResponse(e.getMessage());
                }
            }
            case LogoutRequest logoutRequest -> {
                System.out.println("Logout request ...");

                UserDTO userDTO2 = logoutRequest.user();
                User user = DTOUtils.getFromDTO(userDTO2);

                try {
                    server.logout(user, this);
                    connected = false;
                    return new OkResponse();
                }
                catch (GameException e) {
                    logger.error("Error while processing request", e);
                    return new ErrorResponse(e.getMessage());
                }
            }
            case SaveGameRequest saveGameRequest -> {
                System.out.println("Save game request ...");

                GameDTO gameDTO = saveGameRequest.game();
                Game game = DTOUtils.getFromDTO(gameDTO);

                try {
                    server.saveGame(game);
                    return new OkResponse();
                }
                catch (GameException e) {
                    logger.error("Error while processing request", e);
                    return new ErrorResponse(e.getMessage());
                }
            }
            case GetGamesRequest getGamesRequest -> {
                System.out.println("Get games request ...");

                try {
                    var games = server.getGames().toArray(new Game[0]);
                    var gamesDTO = DTOUtils.getDTO(games);
                    return new GetGamesResponse(gamesDTO);
                }
                catch (GameException e) {
                    logger.error("Error while processing request", e);
                    return new ErrorResponse(e.getMessage());
                }
            }
            case null, default -> {
            }
        }

        return null;
    }

}
