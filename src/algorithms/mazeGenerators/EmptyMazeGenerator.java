package algorithms.mazeGenerators;

/**
 * Implementation of a maze generator algorithm.
 *
 * <p>Generates an empty maze (with no walls)
 * and picks the starting position to be in the top left corner
 * and the goal position to be in the bottom right corner.</p>
 */
public class EmptyMazeGenerator extends AMazeGenerator {


    /**
     * @param rows the number of rows the maze will have.
     * @param cols the number of columns the maze will have.
     * @return an empty maze (with no walls) where the starting position
     * is in the top left corner and the goal is in the bottom right corner.
     */
    @Override
    public Maze generate(int rows, int cols) throws IllegalArgumentException {
        if (!validateInput(rows, cols)) throw new IllegalArgumentException("cannot create maze with " + cols + " columns and " + rows + " rows");
        reset();
        int[][] maze = new int[rows][cols];

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                maze[x][y] = 0;
            }
        }

        return new Maze(maze, new Position(0, 0), new Position(rows - 1, cols - 1));
    }
}
