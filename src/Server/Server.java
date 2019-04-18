package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;
    private int maxListenTime;
    private volatile boolean isRunning;

    private IServerStrategy strategy;

    public Server(int port, int maxListenTime, IServerStrategy serverStrategy) {
        this.port = port;
        this.maxListenTime = maxListenTime;
        this.strategy = serverStrategy;
    }

    public void start() {
        try {
            //TODO: add thread pool support.
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(maxListenTime);
            this.isRunning = true;
            System.out.println("Server started at " + serverSocket);
            System.out.println("Server strategy: " + strategy);
            System.out.println("Server is waiting for clients...");
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connection established at " + clientSocket);
                try {
                    strategy.executeStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
                    clientSocket.getInputStream().close();
                    clientSocket.getOutputStream().close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.isRunning = false;
        System.out.println("Server successfully closed.");
    }


}