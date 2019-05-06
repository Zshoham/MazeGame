package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;


public class ServerStrategySolveSearchProblem implements IServerStrategy {

    private static ISearchingAlgorithm solver = new BestFirstSearch();

    private ISearchable domain;

    @Override
    public void executeStrategy(InputStream inFromClient, OutputStream outToClient) {
        //receive the domain (maze) and return the solution.
        try {
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            toClient.flush();

            //receive the maze from the client.
            Maze clientMaze = (Maze)fromClient.readObject();
            domain = new SearchableMaze(clientMaze);

            //solve the maze.
            Solution solution = solver.solve(domain);

            //return the solution to the client.
            toClient.writeObject(solution);
            toClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
