package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import static Server.Configurations.LOG;

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
        new Thread(this::run, "Server Thread").start();
    }


    private void run() {
        clientPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        clientPool.setCorePoolSize(Configurations.getThreadCount());
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(maxListenTime);
            this.isRunning.set(true);
            LOG.info("Server started at" + serverSocket);
            LOG.info("Server strategy: " + strategy);
            LOG.info("Server is waiting for clients...");

            while (isRunning.get()) {
                Socket clientSocket = serverSocket.accept();
                clientPool.execute(new ClientTask(clientSocket, strategy));
            }
            serverSocket.close();
        } catch (SocketTimeoutException e) {
            if (isRunning.get()) LOG.debug("Server timeout, no clients trying to connect: \n" , e);
        } catch (IOException e) {
            LOG.error("Error establishing connection: \n", e);
        }
    }

    public void stop() {
        this.isRunning.set(false);
        clientPool.shutdown();
        this.strategy.finalizeStrategy();

        LOG.info("Server successfully closed.");
    }


    /**
     * Client Task of the server, executed each time a new connection is established.
     */
    private static class ClientTask implements Runnable {

        private Socket clientSocket;
        private volatile IServerStrategy strategy;

        public ClientTask(Socket clientSocket, IServerStrategy strategy) {
            this.clientSocket = clientSocket;
            this.strategy = strategy;
        }

        @Override
        public void run() {
            LOG.info("Client connection established at " + clientSocket);
            try {
                strategy.executeStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
                clientSocket.close();
            } catch (Exception e) {
                LOG.error("Error Handling Client :", e);
            }
            LOG.info("Client Disconnected from Server");
        }
    }

}
