package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;
import java.util.Arrays;


public class ServerStrategySolveSearchProblem implements IServerStrategy {

    private static ISearchingAlgorithm solver = Configurations.getMazeSolver();

    private static String mazeCacheDir = System.getProperty("java.io.tmpdir") + "/MazeGame";


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

            //check if the clientMaze was already solved, if so return  that solution.
            Solution solution = getCachedSolution(clientMaze);

            if (solution == null) {
                //solve the maze.
                solution = solver.solve(domain);

                //cache the new solution.
                cacheSolution(clientMaze, solution);
            }

            //return the solution to the client.
            toClient.writeObject(solution);
            toClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finalizeStrategy() {
        if (!clearCache()) System.out.println("Unable to delete cache.");
        System.out.println("Maze Solver shutting down.");
    }

    private void cacheSolution(Maze maze, Solution solution) {
        try {
            File cacheFile = new File(mazeCacheDir);
            if (!cacheFile.exists()) cacheFile.mkdirs();

            int cacheHashCode = Arrays.hashCode(maze.toByteArray());

            FileOutputStream fos = new FileOutputStream(mazeCacheDir + "/" + cacheHashCode);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            System.out.println(Arrays.hashCode(maze.toByteArray()));
            oos.writeObject(solution);
            oos.flush();
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Solution getCachedSolution(Maze maze) {
        Solution solution = null;
        int mazeHashCode = Arrays.hashCode(maze.toByteArray());

        try {
            File cacheDir = new File(mazeCacheDir);
            File[] mazeCaches = cacheDir.listFiles();
            if (mazeCaches == null) return null;
            for (File mazeCache : mazeCaches) {
                int fileMazeHash = Integer.parseInt(mazeCache.getName());
                if (mazeHashCode == fileMazeHash) {
                    FileInputStream fis = new FileInputStream(mazeCacheDir + "/" + fileMazeHash);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    solution = (Solution) ois.readObject();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return solution;
    }

    private boolean clearCache() {
        File cacheDir = new File(mazeCacheDir);
        File[] cacheFiles = cacheDir.listFiles();

        boolean result = true;
        if (cacheFiles != null) {
            for (File cacheFile : cacheFiles) {
                result = result && cacheFile.delete();
            }
        }

        return result && cacheDir.delete();
    }
}
