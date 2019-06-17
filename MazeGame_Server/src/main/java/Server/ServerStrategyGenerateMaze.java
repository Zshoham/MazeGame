package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.IMazeGenerator;
import static Server.Configurations.LOG;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    @Override
    public void executeStrategy(InputStream inFromClient, OutputStream outToClient) {
        IMazeGenerator generator = Configurations.getNewMazeGenerator();
        //receive the width and height of the maze and return a compressed maze.
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();

            //receive the size of the maze from client where mazeSize[0] - number of rows, mazeSize[1] - number of columns.
            int[] mazeSize = (int[]) fromClient.readObject();
            if (mazeSize.length != 2) {
                LOG.warn("Client sent invalid message");
                return;
            }

            //serialize the maze.
            byte[] mazeData = generator
                    .generate(mazeSize[Configurations.mazeSizesRowsIndex], mazeSize[Configurations.mazeSizesColsIndex])
                    .toByteArray();

            //compress the maze and send it to the client.
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(toClient);
            compressor.write(mazeData);
            toClient.flush();

        } catch (IOException | ClassNotFoundException e) {
            LOG.error("Error generating maze: \n", e);
        }
    }

    @Override
    public void finalizeStrategy() {
        LOG.info("Maze Generator shutting down.");
    }

    @Override
    public String toString() {
        return "Generate Maze";
    }
}
