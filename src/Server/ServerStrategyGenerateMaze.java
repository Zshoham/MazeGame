package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    private static IMazeGenerator generator = new MyMazeGenerator();

    @Override
    public void executeStrategy(InputStream inFromClient, OutputStream outToClient) {
        //receive the width and height of the maze and return a compressed maze.
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();

            //receive the size of the maze from client where mazeSize[0] - number of rows, mazeSize[1] - number of columns.
            int[] mazeSize = (int[]) fromClient.readObject();
            if (mazeSize.length != 2) System.out.println("ERROR: client sent invalid message");

            //serialize the maze.
            byte[] mazeData = generator.generate(mazeSize[0], mazeSize[1]).toByteArray();

            //compress the maze and write it to the output stream.
            MyCompressorOutputStream compressor = new MyCompressorOutputStream(toClient);
            compressor.write(mazeData);
            toClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
