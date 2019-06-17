package Client;

import Server.Configurations;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private InetAddress serverIP;
    private int serverPort;

    private IClientStrategy strategy;

    public Client(InetAddress ip, int port, IClientStrategy clientStrategy) {
        this.serverIP = ip;
        this.serverPort = port;
        this.strategy = clientStrategy;
    }

    public void communicateWithServer() {
        try {
            Socket server = new Socket(serverIP, serverPort);
            Configurations.getLogger().info("Client is connected to server.");
            strategy.clientStrategy(server.getInputStream(), server.getOutputStream());
            server.close();
        } catch (IOException e) {
            Configurations.getLogger().error("Error communicating with server: \n", e);
        }
    }
}
