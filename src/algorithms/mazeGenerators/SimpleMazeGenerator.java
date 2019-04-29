package algorithms.mazeGenerators;

import java.util.Random;

/**
 * Implementation of a maze generator algorithm.
 *
 * <p>Generates a almost completely random 'maze', for each cell it is randomly decided
 * if the cell will be a wall or not.
 * The start and goal positions are chosen randomly and then a path is built between them.</p>
 */
public class SimpleMazeGenerator extends AMazeGenerator {

    private Random random;

    /**
     * @param rows the number of rows the maze will have.
     * @param cols the number of columns the maze will have.
     * @return a maze where the walls are randomly generated,
     * and a path from start to goal is guaranteed.
     */
    @Override
    public Maze generate(int rows, int cols) throws IllegalArgumentException {
        if (!validateInput(rows, cols)) throw new IllegalArgumentException("cannot create maze with " + cols + " columns and " + rows + " rows");
        reset();
        random = new Random(System.currentTimeMillis());

        int[][] maze = new int[rows][cols];

        boolean isWall;
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                isWall = random.nextBoolean();
                maze[y][x] = isWall ? 1 : 0;

            }
        }

        pickStart(rows, cols);
        pickGoal(rows, cols);

        maze[start.getRowIndex()][start.getColumnIndex()] = 0;
        maze[goal.getRowIndex()][goal.getColumnIndex()] = 0;

        buildSolution(maze, this.start, this.goal);

        return new Maze(maze, this.start, this.goal);
    }

    /**
     * Builds a path in the maze from current to goal.
     *
     * @param maze  the maze where the path will be built.
     * @param start the starting position of the path.
     * @param goal  the end position of the path.
     */
    private void buildSolution(int[][] maze, Position start, Position goal) {
        if (start.equals(goal)) return;
        start = getClosestToEnd(start, goal);
        maze[start.getRowIndex()][start.getColumnIndex()] = 0;
        buildSolution(maze, start, goal);
    }

    /**
     * Picks a neighbor of current closest to the goal position.
     *
     * @param current the current position of the path generator.
     * @param goal    the goal position of the maze.
     * @return the neighbor position closest to goal from current's neighbors.
     */
    private Position getClosestToEnd(Position current, Position goal) {
        Position[] neighbors = new Position[4];
        Double[] distances = new Double[4];
        neighbors[0] = new Position(current.getRowIndex() - 1, current.getColumnIndex()); //up
        neighbors[1] = new Position(current.getRowIndex(), current.getColumnIndex() + 1); //right
        neighbors[2] = new Position(current.getRowIndex() + 1, current.getColumnIndex()); //down
        neighbors[3] = new Position(current.getRowIndex(), current.getColumnIndex() - 1); //left

        for (int i = 0; i < 4; i++)
            distances[i] = goal.getDistance(neighbors[i]);
        int closest = getMin(distances);
        return neighbors[closest];
    }

    /**
     * @param arr array of double values.
     * @return the smallest value in the array.
     */
    private int getMin(Double[] arr) {
        int minI = 0;

        for (int i = 0; i < 4; i++)
            if (arr[i] < arr[minI])
                minI = i;
        return minI;
    }


}
