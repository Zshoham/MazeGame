package Server;

import algorithms.search.ISearchable;
import algorithms.search.ISearchingAlgorithm;

import java.io.InputStream;
import java.io.OutputStream;


public class ServerStrategySolveSearchProblem implements IServerStrategy {

    private ISearchable domain;
    private ISearchingAlgorithm solver;

    @Override
    public void executeStrategy(InputStream inFromClient, OutputStream outToClient) {
        //receive the domain (maze) and return the solution.
    }
}
