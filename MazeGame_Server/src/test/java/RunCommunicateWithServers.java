import IO.MyDecompressorInputStream;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;
import org.apache.logging.log4j.LogManager;

import javax.security.auth.login.Configuration;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Aviadjo on 3/27/2017.
 */
public class RunCommunicateWithServers {

    RunCommunicateWithServers() {
        Configurations.setConfigPath(getClass().getResource("/").getPath() + "config.properties");
        Configurations.setLog(LogManager.getLogger("name"));
        Configurations.setProperty("Delete_Cache_On_Close", "T");
    }

    public static void main(String[] args) {
        RunCommunicateWithServers t = new RunCommunicateWithServers();
        //Initializing servers
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        //Server stringReverserServer = new Server(5402, 1000, new ServerStrategyStringReverser());

        //Starting  servers
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
        //stringReverserServer.start();

        //Communicating with servers
        CommunicateWithServer_MazeGenerating();
        CommunicateWithServer_SolveSearchProblem();
        //CommunicateWithServer_StringReverser();

        //Stopping all servers
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
        //stringReverserServer.stop();
    }

    private static void CommunicateWithServer_MazeGenerating() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, (inFromServer, outToServer) -> {
                try {
                    int mazeX = 50;
                    int mazeY = 50;
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    int[] mazeDimensions = new int[]{mazeY, mazeX};
                    toServer.writeObject(mazeDimensions); //send maze dimensions to server
                    toServer.flush();
                    byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                    byte[] decompressedMaze = new byte[mazeY*mazeX + Maze.HEADER_LENGTH /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                    is.read(decompressedMaze); //Fill decompressedMaze with bytes
                    Maze maze = new Maze(decompressedMaze);
                    maze.print();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void CommunicateWithServer_SolveSearchProblem() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, (inFromServer, outToServer) -> {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    MyMazeGenerator mg = new MyMazeGenerator();
                    Maze maze = mg.generate(50, 50);
                    maze.print();
                    toServer.writeObject(maze); //send maze to server
                    toServer.flush();
                    Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server

                    //Print Maze Solution retrieved from the server
                    System.out.println(String.format("Solution steps: %s", mazeSolution));
                    ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                    for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                        System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void CommunicateWithServer_StringReverser() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5402,  (inFromServer, outToServer) -> {
                try {
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(inFromServer));
                    PrintWriter toServer = new PrintWriter(outToServer);

                    String message = "Client Message";
                    String serverResponse;
                    toServer.write(message + "\n");
                    toServer.flush();
                    serverResponse = fromServer.readLine();
                    System.out.println(String.format("Server response: %s", serverResponse));
                    toServer.flush();
                    fromServer.close();
                    toServer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
