package algorithms.mazeGenerators;

/**
 * Interface describing the common functionality of all maze generation algorithms.
 */
public interface IMazeGenerator {

    /**
     * Generate a maze with of size rows x cols.
     *
     * @param rows the number of rows the maze will have.
     * @param cols the number of columns the maze will have.
     * @return the generated maze.
     * @exception IllegalArgumentException if the size of the maze is too small an exception will be throws.
     */
    Maze generate(int rows, int cols) throws IllegalArgumentException;

    /**
     * Measure the time it takes to generate a maze.
     *
     * @param rows the number of rows in the maze that is being tested.
     * @param cols the number of columns in the maze that is being tested.
     * @return the time in milliseconds it took the program to generate the mze.
     */
    long measureAlgorithmTimeMillis(int rows, int cols);
}
