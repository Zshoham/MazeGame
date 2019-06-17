package Server;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface describing the common functionality of a Server Strategy.
 */
public interface IServerStrategy {

    /**
     * Executes the server strategy toward the client.
     * @param inFromClient the stream containing the input from the client.
     * @param outToClient the stream containing the servers output to the client.
     */
    void executeStrategy(InputStream inFromClient, OutputStream outToClient);

    /**
     * Alerts the strategy that the server is shutting down
     */
    void finalizeStrategy();

}
