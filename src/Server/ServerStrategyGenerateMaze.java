package Server;

import algorithms.mazeGenerators.IMazeGenerator;

import java.io.InputStream;
import java.io.OutputStream;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    private IMazeGenerator generator;

    @Override
    public void executeStrategy(InputStream inFromClient, OutputStream outToClient) {
        //receive the width and height of the maze and return a compressed maze.
    }
}
