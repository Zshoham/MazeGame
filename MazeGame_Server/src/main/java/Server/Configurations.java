package Server;

import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.BestFirstSearch;
import algorithms.search.BreadthFirstSearch;
import algorithms.search.DepthFirstSearch;
import algorithms.search.ISearchingAlgorithm;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public final class Configurations {

    private Configurations() {}

    static Logger LOG;

    public static Logger getLogger() { return LOG; }

    public static void setLog(Logger logger) {
        if (LOG == null)
            LOG = logger;
    }

    private static ServerProperties properties;

    public static void setConfigPath(String path) { properties = new ServerProperties(path); }

    /**
     * @return the number of threads that the server should have in the thread pool.
     */
    public static int getThreadCount() {
        if(!properties.isCPUThreads) return properties.threadCount;
        else return Runtime.getRuntime().availableProcessors() * properties.threadCount;
    }

    public static String getMazeGenerator() { return properties.getMazeGeneratorProp(); }

    public static String getMazeSolver() { return properties.getMazeSolverProp(); }

    public synchronized static IMazeGenerator getNewMazeGenerator() { return properties.getNewMazeGenerator(); }

    public synchronized static ISearchingAlgorithm getNewMazeSolver() { return properties.getNewMazeSolver(); }

    public static boolean shouldDeleteCache() { return properties.deleteCache; }

    public static ArrayList<String> getMazeGenerators() {
        ArrayList<String> res = new ArrayList<String>();
        res.add("MyMazeGenerator");
        res.add("SimpleMazeGenerator");
        res.add("EmptyMazeGenerator");


        return res;
    }

    public static ArrayList<String> getMazeSolvers() {
        ArrayList<String> res = new ArrayList<String>();
        res.add("BestFirstSearch");
        res.add("BreadthFirstSearch");
        res.add("DepthFirstSearch");

        return res;
    }

    public static void setProperty(String property, String value) {
        properties.setProperty(property, value);
    }

    /**
     * The index of the array the Generate Maze Strategy gets where the numbers of rows in the maze is stores.
     */
    public static final int mazeSizesRowsIndex = 0;

    /**
     * The index of the array the Generate Maze Strategy gets where the numbers of columns in the maze is stores.
     */
    public static final int mazeSizesColsIndex = 1;

    public static boolean DEBUG = false;

    /**
     * A wrapper class that stores the configurations and handles their storage.
     */
    private static class ServerProperties {

        private static final String description = " Server Properties File\n" +
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
        private String mazeGeneratorProp;
        private String mazeSolverProp;
        private boolean deleteCache;
        private String path;

        public ServerProperties(String path) {
            this.path = path;
            File propFile = new File(path);
            if (propFile.exists()) readProperties();
            else initProperties();
        }

        private void initProperties() {
            try {
                FileOutputStream fos = new FileOutputStream(this.path);

                Properties properties = new Properties();
                properties.setProperty("Server_Thread_Count", "cpu,2");
                this.isCPUThreads = true;
                this.threadCount = 2;
                properties.setProperty("Maze_Generation_Algorithm", "MyMazeGenerator");
                properties.setProperty("Maze_Solver_Algorithm", "BestFirstSearch");
                properties.setProperty("Delete_Cache_On_Close", "F");
                this.deleteCache = false;

                properties.store(fos, description);

                fos.close();

            } catch (IOException e) {
                LOG.error("Error creating properties file: \n", e);
                return;
            }
            this.readProperties();
        }

        private void readProperties() {
            try {
                FileInputStream fis = new FileInputStream(this.path);

                Properties properties = new Properties();
                properties.load(fis);


                parseThreadCount((String) properties.get("Server_Thread_Count"));
                this.mazeGeneratorProp = (String) properties.get("Maze_Generation_Algorithm");
                this.mazeSolverProp = (String) properties.get("Maze_Solver_Algorithm");
                parseDeleteCache((String) properties.get("Delete_Cache_On_Close"));

                fis.close();

            } catch (FileNotFoundException e) {
                LOG.error("Error, cant find properties file: ", e);
            }
            catch (Exception e) {
               LOG.error("Error reading properties file: \n", e);
               initProperties();
               LOG.info("reverting to default properties.");
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

        private void parseDeleteCache(String value) {
            if (value.toLowerCase().equals("t")) this.deleteCache = true;
            else if (value.toLowerCase().equals("f")) this.deleteCache = false;
            else throw new RuntimeException("The value of the 'Delete_Cache_On_Close' property is invalid.");
        }

        public IMazeGenerator getNewMazeGenerator() {
            if (this.mazeGeneratorProp.toLowerCase().equals(("MyMazeGenerator").toLowerCase())) return new MyMazeGenerator();
            else if (this.mazeGeneratorProp.toLowerCase().equals(("SimpleMazeGenerator").toLowerCase())) return new SimpleMazeGenerator();
            else if (this.mazeGeneratorProp.toLowerCase().equals(("EmptyMazeGenerator").toLowerCase())) return new EmptyMazeGenerator();
            else throw new RuntimeException("The value of the 'Maze Generator Algorithm' property is invalid.");
        }

        public ISearchingAlgorithm getNewMazeSolver() {
            if (this.mazeSolverProp.toLowerCase().equals(("BestFirstSearch").toLowerCase())) return new BestFirstSearch();
            else if (this.mazeSolverProp.toLowerCase().equals(("BreadthFirstSearch").toLowerCase())) return new BreadthFirstSearch();
            else if (this.mazeSolverProp.toLowerCase().equals(("DepthFirstSearch").toLowerCase())) return new DepthFirstSearch();
            else throw new RuntimeException("The value of the 'Maze_Solver_Algorithm' property is invalid.");
        }

        public void setProperty(String property, String value) {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(this.path));
                properties.put(property, value);
                properties.store(new FileOutputStream(this.path), description);
            } catch (FileNotFoundException e) {
                LOG.error("Error, cant find properties file: ", e);
                return;
            }
            catch (IOException e) {
                LOG.error("Error editing properties file: \n", e);
                initProperties();
            }
            readProperties();
        }

        public String getMazeGeneratorProp() { return this.mazeGeneratorProp; }
        public String getMazeSolverProp() { return this.mazeSolverProp; }
    }
}
