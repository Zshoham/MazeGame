package algorithms.mazeGenerators;

import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {

    private Random random;

    @Override
    public Maze generate(int rows, int cols) {
        random = new Random(System.currentTimeMillis());

        Position start = chooseEdgePosition(rows, cols);
        Position goal;
        do {
            goal = chooseEdgePosition(rows, cols);
        } while(goal.equals(start));

        int[][] maze = new int[rows][cols];

        maze[start.getColumnIndex()][start.getRowIndex()] = -1;
        maze[goal.getColumnIndex()][goal.getRowIndex()] = -1;

        boolean isWall;
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if(maze[x][y] != -1) {
                    isWall = random.nextBoolean();
                    maze[x][y] = isWall ? 1 : 0;
                }
            }
        }

        maze[start.getColumnIndex()][start.getRowIndex()] = 0;
        maze[goal.getColumnIndex()][goal.getRowIndex()] = 0;

        //TODO: check if maze needs to be solvable, if so add a code to carve a path from start to goal.

        return new Maze(maze, start, goal);
    }

    private Position chooseEdgePosition(int rows, int cols)
    {
        int edge = random.nextInt(4);
        int edgeOffset;
        Position res = null;
        switch (edge){
            case 0:
                edgeOffset = random.nextInt(cols);
                res = new Position(0, edgeOffset);
                break;
            case 1:
                edgeOffset = random.nextInt(cols);
                res = new Position(rows - 1, edgeOffset);
                break;
            case 2:
                edgeOffset = random.nextInt(rows);
                res = new Position(edgeOffset, 0);
                break;
            case 3:
                edgeOffset = random.nextInt(rows);
                res = new Position(edgeOffset, cols - 1);
                break;

        }

        return res;
    }
}
