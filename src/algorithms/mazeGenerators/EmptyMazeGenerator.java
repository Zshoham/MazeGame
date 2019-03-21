package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {


    @Override
    public Maze generate(int rows, int cols) {

        int[][] maze = new int[rows][cols];

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                maze[x][y] = 0;
            }
        }

        return new Maze(maze, new Position(0,0), new Position(rows - 1, cols - 1));
    }
}
