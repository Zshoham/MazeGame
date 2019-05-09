package Server;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;

import java.io.*;
import java.util.Properties;

public final class Configurations {

    private static String configPath = "resources/config.properties";

    private Configurations() {}

    private static ServerProperties properties = new ServerProperties(configPath);

    /**
     * @return the number of threads that the server should have in the thread pool.
     */
    public static int getThreadCount() {
        if(!properties.isCPUThreads) return properties.threadCount;
        else return Runtime.getRuntime().availableProcessors() * properties.threadCount;
    }

    public static IMazeGenerator getMazeGenerator() { return properties.mazeGenerator; }

    public static ISearchingAlgorithm getMazeSolver() { return properties.mazeSolver; }

    public static boolean shouldDeleteCache() { return properties.deleteCache; }

    /**
     * The index of the array the Generate Maze Strategy gets where the numbers of rows in the maze is stores.
     */
    public static final int mazeSizesRowsIndex = 0;

    /**
     * The index of the array the Generate Maze Strategy gets where the numbers of columns in the maze is stores.
     */
    public static final int mazeSizesColsIndex = 1;

    public static final boolean DEBUG = false;

    /**
     * A wrapper class that stores the configurations and handles their storage.
     */
    private static class ServerProperties {

        private static String description = " Server Properties File\n" +
                " Server_Thread_Count - may be a number or cpu,number meaning the server will have number*" +
                " times the available cpu cores.\n" +
                " Maze_Generation_Algorithm - may be one of the following:\n" +
                "   * MyMazeGenerator.\n" +
                "   * SimpleMazeGenerator.\n" +
                "   * EmptyMazeGenerator.\n" +
                " Maze_Solver_Algorithm - may be one of the following:\n" +
                "   * BestFirstSearch.\n" +
                "   * BreadthFirstSearch.\n" +
                "   * DepthFirstSearch.\n" +
                " Delete_Cache_On_Close - may be one of the following:\n" +
                "   * T - when the solver strategy is finalized it will delete the solution cache.\n" +
                "   * F - the solver strategy's maze cache will not be affected by server shutdown.\n";

        private boolean isCPUThreads;
        private int threadCount;
        private IMazeGenerator mazeGenerator;
        private ISearchingAlgorithm mazeSolver;
        private boolean deleteCache;

        public ServerProperties(String path) {
            File propFile = new File(path);
            if (propFile.exists()) readProperties(path);
            else initProperties(path);
        }

        private void readProperties(String path) {
            try {
                FileInputStream fis = new FileInputStream(path);

                Properties properties = new Properties();
                properties.load(fis);


                parseThreadCount((String) properties.get("Server_Thread_Count"));
                parseMazeGenerator((String) properties.get("Maze_Generation_Algorithm"));
                parseMazeSolver((String) properties.get("Maze_Solver_Algorithm"));
                parseDeleteCache((String) properties.get("Delete_Cache_On_Close"));

                fis.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void initProperties(String path) {
            try {
                FileOutputStream fos = new FileOutputStream(path);

                Properties properties = new Properties();
                properties.setProperty("Server_Thread_Count", "cpu,2");
                this.isCPUThreads = true;
                this.threadCount = 2;
                properties.setProperty("Maze_Generation_Algorithm", "MyMazeGenerator");
                this.mazeGenerator = new MyMazeGenerator();
                properties.setProperty("Maze_Solver_Algorithm", "BestFirstSearch");
                this.mazeSolver = new BestFirstSearch();
                properties.setProperty("Delete_Cache_On_Close", "F");
                this.deleteCache = false;

                properties.store(fos, description);

                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void parseThreadCount(String value) {
            String[] threadsSubProps = value.split(",");
            if(threadsSubProps.length == 1) {
                this.isCPUThreads = false;
                this.threadCount = Integer.parseInt(threadsSubProps[0]);
            }
            else if (threadsSubProps.length == 2) {
                if (!threadsSubProps[0].equals("cpu"))
                    throw new RuntimeException("The format of the 'Server Thread Count' property was incorrect.");
                this.threadCount = Integer.parseInt(threadsSubProps[1]);
                this.isCPUThreads = true;
            }
            else
                throw new RuntimeException("The format of the 'Server Thread Count' property was incorrect.");

        }

        private void parseMazeSolver(String value) {
            if (value.toLowerCase().equals(("BestFirstSearch").toLowerCase())) this.mazeSolver = new BestFirstSearch();
            else if (value.toLowerCase().equals(("BreadthFirstSearch").toLowerCase())) this.mazeSolver = new BreadthFirstSearch();
            else if (value.toLowerCase().equals(("DepthFirstSearch").toLowerCase())) this.mazeSolver = new DepthFirstSearch();
            else throw new RuntimeException("The value of the 'Maze_Solver_Algorithm' property is invalid.");
        }

        private void parseMazeGenerator(String value) {
            if (value.toLowerCase().equals(("MyMazeGenerator").toLowerCase())) this.mazeGenerator = new MyMazeGenerator();
            else if (value.toLowerCase().equals(("SimpleMazeGenerator").toLowerCase())) this.mazeGenerator = new SimpleMazeGenerator();
            else if (value.toLowerCase().equals(("EmptyMazeGenerator").toLowerCase())) this.mazeGenerator = new EmptyMazeGenerator();
            else throw new RuntimeException("The value of the 'Maze Generator Algorithm' property is invalid.");
        }

        private void parseDeleteCache(String value) {
            if (value.toLowerCase().equals("t")) this.deleteCache = true;
            else if (value.toLowerCase().equals("f")) this.deleteCache = false;
            else throw new RuntimeException("The value of the 'Delete_Cache_On_Close' property is invalid.");
        }
    }
}
