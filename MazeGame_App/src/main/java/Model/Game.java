package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.Configurations;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import View.DisplayableMaze;
import View.ResourceManager;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

import Model.Character.CHARACTER_MOVEMENT;
import Model.Character.CHARACTER_TYPE;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

import static Model.GamePersistenceManager.LOG;

public class Game extends Observable implements IModel {


    private static final int generatorPort = 5400, solverPort = 5401;

    public enum GAMESTATE { CLOSED, RUNNING, PAUSED, SOLVED }

    private Server mazeGenerator, mazeSolver;

    private Maze maze;
    private Solution solution;
    private Character character;
    private volatile GAMESTATE gamestate;


    protected Maze getMaze() { return this.maze; }
    protected Character getCharacter() { return this.character; }

    public Game() {
        Configurations.setLog(LogManager.getLogger("Server_Log"));
        if (Configurations.DEBUG) {
            Configurations.setConfigPath(getClass().getResource("/").getPath() + "config.properties");
        }
        else {
            Configurations.setConfigPath(new File("") + "config.properties");
        }
        mazeGenerator = new Server(generatorPort, 0 /* infinite timeout */, new ServerStrategyGenerateMaze());
        mazeSolver = new Server(solverPort, 0 /* infinite timeout */, new ServerStrategySolveSearchProblem());
        gamestate = GAMESTATE.CLOSED;

        mazeGenerator.start();
        mazeSolver.start();
    }



    protected void load(Maze maze, Character character) {
        this.maze = maze;
        this.character = character;
        this.gamestate = GAMESTATE.RUNNING;

        solveMaze();

        ResourceManager.getInstance().loadCharacter(character.getType());

        new Thread(this::run, "Game Thread").start();

        setChanged();
        notifyObservers(new DisplayableMaze(maze, solution));
    }

    public void saveGame(String saveName) {
        GamePersistenceManager.getInstance().saveGame(this, saveName);
    }

    public void loadGame(String save) { GamePersistenceManager.getInstance().loadGame(this, save); }

    public synchronized void start(CHARACTER_TYPE type, int width, int height) {
        generateMaze(width, height);
        solveMaze();
        this.character = new Character(type, this.maze.getStartPosition(), new CollisionMaze(this.maze));
        this.gamestate = GAMESTATE.RUNNING;

        new Thread(this::run, "Game Thread").start();

        setChanged();
        notifyObservers(new DisplayableMaze(maze, solution));
    }

    public void togglePause() {
        //toggle pause the game.
        if (this.gamestate == GAMESTATE.PAUSED) this.gamestate = GAMESTATE.RUNNING;
        else  {
            gamestate = GAMESTATE.PAUSED;
            character.setMovement(CHARACTER_MOVEMENT.IDLE);
        }
        setChanged();
        notifyObservers(this.gamestate);
    }

    public void close() {
        //closed the game window but did not exit the game.
        this.gamestate = GAMESTATE.CLOSED;
        setChanged();
        notifyObservers(gamestate);
    }

    public void exit() {
        mazeGenerator.stop();
        mazeSolver.stop();
    }

    private void run() {
        int u_time = 0;
        long lastUpdateTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        int desiredUPS = 60;
        double ns = 1000000000.0/desiredUPS;
        double delta = 0.0;
        while (this.gamestate != GAMESTATE.CLOSED) {
            long now = System.nanoTime();
            delta += (now - lastUpdateTime) / ns;
            lastUpdateTime = System.nanoTime();
            while(delta >= 1){
                character.update(u_time);
                setChanged();
                if (isSolved()) {
                    this.gamestate = GAMESTATE.CLOSED;
                    notifyObservers(GAMESTATE.SOLVED);
                }
                else notifyObservers(CHARACTER_MOVEMENT.DOWN);
                delta--;
                u_time++;
            }
            if(System.currentTimeMillis() - timer >= 1000){
                timer += 1000;
            }
        }
    }

    private boolean isSolved() {
        return this.character.getMazeTile().equals(this.maze.getGoalPosition());
    }

    private void generateMaze(int width, int height) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), generatorPort, (inFromServer, outToServer) -> {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    int[] mazeDimensions = new int[] {height, width};
                    toServer.writeObject(mazeDimensions);
                    toServer.flush();
                    byte[] compressedMaze = (byte[]) fromServer.readObject();
                    InputStream decompressor = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                    byte[] decompressedMaze = new byte[height*width + Maze.HEADER_LENGTH];
                    decompressor.read(decompressedMaze);
                    this.maze = new Maze(decompressedMaze);
                } catch (IOException | ClassNotFoundException e) {
                   LOG.error("Error generating maze: \n", e);
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            LOG.error("Error identifying local host: \n", e);
        }
    }

    private void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), solverPort, (inFromServer, outToServer) -> {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    toServer.writeObject(this.maze);
                    toServer.flush();
                    this.solution = (Solution)fromServer.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    LOG.error("Error solving maze: \n", e);
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            LOG.error("Error identifying local host: \n", e);
        }
    }

    @Override
    public void moveCharacter(CHARACTER_MOVEMENT movement) {
        character.setMovement(movement);
    }

    @Override
    public double getCharacterX() { return character.getCharacterX(); }

    @Override
    public double getCharacterY() { return character.getCharacterY(); }

    @Override
    public Position getCharacterPosition() { return this.character.getMazeTile(); }

    @Override
    public int getCharacterFrame() { return character.getFrame(); }

}
