package ro.mpp2024.networking.utils;

import ro.mpp2024.networking.objectprotocol.GameClientObjectWorker;
import ro.mpp2024.services.GameServices;

import java.net.Socket;

public class GameObjectConcurrentServer extends AbsConcurrentServer {
    private final GameServices transportServer;

    public GameObjectConcurrentServer(int port, GameServices transportServer) {
        super(port);
        this.transportServer = transportServer;
        System.out.println("Game - GameObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        GameClientObjectWorker worker = new GameClientObjectWorker(transportServer, client);
        return new Thread(worker);
    }
}
