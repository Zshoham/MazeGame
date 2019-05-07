package Server;

import java.io.InputStream;
import java.io.OutputStream;

public interface IServerStrategy {

    void executeStrategy(InputStream inFromClient, OutputStream outToClient);

    void finalizeStrategy();

}
