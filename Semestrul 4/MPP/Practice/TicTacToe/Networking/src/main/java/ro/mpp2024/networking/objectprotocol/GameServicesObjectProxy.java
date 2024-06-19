package ro.mpp2024.networking.objectprotocol;

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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameServicesObjectProxy implements GameServices {
    private String host;
    private int port;

    private GameObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public GameServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;

        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public User login(User user, GameObserver client) throws GameException {
        initializeConnection();

        UserDTO userDTO = DTOUtils.getDTO(user);

        sendRequest(new LoginRequest(userDTO));
        Response response = readResponse();
        if (response instanceof LoginResponse loginResponse){
            this.client = client;
            return DTOUtils.getFromDTO(loginResponse.userDTO());
        }
        else if (response instanceof ErrorResponse err){
            closeConnection();
            throw new GameException(err.message());
        }
        else {
            closeConnection();
            throw new GameException("Unknown response");
        }
    }

    @Override
    public void logout(User user, GameObserver client) throws GameException {
        UserDTO userDTO = DTOUtils.getDTO(user);

        sendRequest(new LogoutRequest(userDTO));
        Response response = readResponse();

        closeConnection();
        if (response instanceof ErrorResponse err){
            throw new GameException(err.message());
        }
    }

    @Override
    public Collection<Game> getGames() throws GameException {
        sendRequest(new GetGamesRequest());
        Response response = readResponse();

        if (response instanceof GetGamesResponse getGamesResponse){
            var gamesDTO = getGamesResponse.games();
            var games = DTOUtils.getFromDTO(gamesDTO);
            return List.of(games);
        }
        else if (response instanceof ErrorResponse err){
            throw new GameException(err.message());
        }
        else {
            throw new GameException("Unknown response");
        }
    }

    @Override
    public void saveGame(Game game) throws GameException {
        GameDTO reservationDTO = DTOUtils.getDTO(game);

        sendRequest(new SaveGameRequest(reservationDTO));
        Response response = readResponse();

        if (response instanceof ErrorResponse err){
            throw new GameException(err.message());
        }
    }


    private void handleUpdate(UpdateResponse update){
        if (update instanceof NewFinishedGameResponse newFinishedGameResponse){
            GameDTO[] gamesDTO = newFinishedGameResponse.games();
            Game[] games = DTOUtils.getFromDTO(gamesDTO);

            try {
                client.newFinishedGame(List.of(games));
            } catch (GameException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRequest(Request request) throws GameException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new GameException("Error sending object " + e);
        }

    }

    private Response readResponse() throws GameException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws GameException {
         try {
            connection = new Socket(host,port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                         handleUpdate((UpdateResponse)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
