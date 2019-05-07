package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {

    private int port;
    private int maxListenTime;
    private volatile AtomicBoolean isRunning;

    private IServerStrategy strategy;

    private ThreadPoolExecutor clientPool;
    private ServerSocket serverSocket;

    public Server(int port, int maxListenTime, IServerStrategy serverStrategy) {
        this.port = port;
        this.maxListenTime = maxListenTime;
        this.strategy = serverStrategy;
        isRunning = new AtomicBoolean(false);
    }

    /**
     * Starts the server.
     */
    public void start() {
        new Thread( () -> run(), "Server Thread").start();
    }


    private void run() {
        clientPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        clientPool.setCorePoolSize(Configurations.getThreadCount());
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(maxListenTime);
            this.isRunning.set(true);
            System.out.println("Server started at " + serverSocket);
            System.out.println("Server strategy: " + strategy);
            System.out.println("Server is waiting for clients...");
            while (isRunning.get()) {
                Socket clientSocket = serverSocket.accept();
                clientPool.execute(new ClientTask(clientSocket, strategy));
            }
            serverSocket.close();
        } catch (SocketTimeoutException e) {
            if (isRunning.get()) e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.isRunning.set(false);
        clientPool.shutdown();
        this.strategy.finalizeStrategy();
        System.out.println("Server successfully closed.");
    }


    /**
     * Client Task of the server, executed each time a new connection is established.
     */
    private static class ClientTask implements Runnable {

        private Socket clientSocket;
        private IServerStrategy strategy;

        public ClientTask(Socket clientSocket, IServerStrategy strategy) {
            this.clientSocket = clientSocket;
            this.strategy = strategy;
        }

        @Override
        public void run() {
            System.out.println("Client connection established at " + clientSocket);
            try {
                strategy.executeStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client Disconnected from Server");
        }
    }

}
