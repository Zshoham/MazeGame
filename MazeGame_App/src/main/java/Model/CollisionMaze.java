package Model;

import algorithms.mazeGenerators.Maze;

public class CollisionMaze extends Maze {


    public CollisionMaze(Maze base) {
        super(base);
    }

    /**
     * Checks if the given position will be mapped to a point on a wall of the maze.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return true if (x, y) is in tile that is a wall.
     */
    public boolean isColliding(double x, double y) {

        if (x < 0 || y < 0) return true;
        if (x > this.cols * 32 || y > this.rows * 32) return true;

        int mazeX = (int)(x / 32);
        int mazeY = (int)(y / 32);

        try { return this.maze[mazeY][mazeX] == 1; }
        catch (IndexOutOfBoundsException e) {
            return true;
        }
    }
}
