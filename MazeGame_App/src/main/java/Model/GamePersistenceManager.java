package Model;

import Server.Configurations;
import algorithms.mazeGenerators.Maze;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class GamePersistenceManager {

    private static GamePersistenceManager instance = new GamePersistenceManager();

    public static GamePersistenceManager getInstance() {
        return instance;
    }

    public static final Logger LOG = LogManager.getLogger("App_Log");

    public static final String FILE_EXTENSION = ".save";

    public static ArrayList<String> getSaveList() {
        ArrayList<String> res = new ArrayList<>();
        String path = getInstance().path;

        File saveDir = new File(path);
        File[] gameSaves = saveDir.listFiles();
        if (gameSaves == null) return res;

        for (File save : gameSaves) {
            res.add(save.getName().substring(0, save.getName().indexOf('.')));
        }

        return res;
    }

    //protected static String path;

    private String path;

    private GamePersistenceManager() {
        if (Configurations.DEBUG) {
            this.path = getClass().getResource("/").getPath() + "saves/";
        }
        else {
            this.path = new File("").getAbsolutePath() + "/saves/";
            File pathFile = new File(path);
            if (!pathFile.exists()) pathFile.mkdirs();
            System.out.println(path);
        }
    }

    public void saveGame(Game game, String saveName) {
        try {
            FileOutputStream fos = new FileOutputStream(path + saveName + FILE_EXTENSION);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(game.getMaze());
            oos.writeObject(game.getCharacter());
            oos.flush();

        } catch (IOException e) {
            LOG.error("Error saving the game: ", e);
        }
    }

    public void loadGame(Game game, String saveName) {
        try {
            FileInputStream fis = new FileInputStream(path + saveName + FILE_EXTENSION);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Maze maze = (Maze) ois.readObject();
            Character character = (Character) ois.readObject();

            game.load(maze, character);

        } catch (IOException e) {
            LOG.error("Error loading the game: ", e);
        } catch (ClassNotFoundException e) {
            LOG.error("Error loading the game, trying to read invalid file: ", e);
        }
    }
}
