package algorithms.mazeGenerators;

import algorithms.search.AState;
import algorithms.search.Solution;

/**
 * Maze wrapper for the maze data.
 * it holds the the array describing the maze
 * and the positions of the start and the goal of the maze.
 */
public class Maze {

    private static final boolean DEBUG = true;

    protected int[][] maze;
    protected int rows;
    protected int cols;
    private Position startPosition;
    private Position goalPosition;

    /**
     * Constructs a new maze.
     * @param maze the array describing the maze.
     * @param startPosition the starting position in the maze.
     * @param goalPosition the goal position of the maze.
     */
    public Maze(int[][] maze, Position startPosition, Position goalPosition) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.startPosition = startPosition;
        this.goalPosition = goalPosition;
    }

    /**
     * Constructs a copy of a maze.
     * @param other the maze to be copied.
     */
    public Maze(Maze other) {
        this.maze = new int[other.maze.length][other.maze[0].length];
        for (int x = 0; x < this.maze.length; x++) {
            System.arraycopy(other.maze[x], 0, this.maze[x], 0, this.maze[0].length);
        }
        this.rows = other.rows;
        this.cols = other.cols;
        this.startPosition = new Position(other.startPosition);
        this.goalPosition = new Position(other.goalPosition);
    }

    /**
     * @return the starting position of the maze.
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * @return the goal position in the maze.
     */
    public Position getGoalPosition() {
        return goalPosition;
    }

    /**
     * Prints the maze to the console. <br/>
     * The start position is the letter S. <br/>
     * The goal position is the letter E. <br/>
     * 1 Represents a wall, and 0 represents an empty cell.
     */
    public void print() {
        if (DEBUG) {
            beautifyPrint();
            return;
        }

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (startPosition.equals(new Position(y, x))) System.out.print("S ");
                else if (goalPosition.equals(new Position(y, x))) System.out.print("E ");
                else System.out.print(maze[y][x] + " ");
            }
            System.out.println();
        }
    }

    private void beautifyPrint() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (i == this.startPosition.getRowIndex() && j == this.startPosition.getColumnIndex()) {//startPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (i == this.goalPosition.getRowIndex() && j == this.goalPosition.getColumnIndex()) {//goalPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (maze[i][j] == 1) System.out.print(" " + "\u001B[45m" + " ");
                else System.out.print(" " + "\u001B[107m" + " ");
            }
            System.out.println(" " + "\u001B[107m");
        }
    }

    public void beautifyPrintSol(Solution sol) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {

                if (i == this.startPosition.getRowIndex() && j == this.startPosition.getColumnIndex()) {//startPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (i == this.goalPosition.getRowIndex() && j == this.goalPosition.getColumnIndex()) {//goalPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (maze[i][j] == 1) System.out.print(" " + "\u001B[45m" + " ");
                else if(sol.contains(i, j))
                    System.out.print(" " + "\u001B[41m" + " ");
                else System.out.print(" " + "\u001B[107m" + " ");
            }
            System.out.println(" " + "\u001B[107m");
        }
    }
}