package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class DisplayableMaze extends Maze {

    public static final int TILE_FLOOR      = 0,
                            TILE_WALL       = 1,
                            TILE_START      = 2,
                            TILE_END        = 3,
                            TILE_SOLUTION   = 4;

    public static final Color COLOR_WALL       = Color.rgb(0, 0, 0, 0.4);
    public static final Color COLOR_FLOOR      = Color.rgb(255,255,255, 0.4);
    public static final Color COLOR_START      = Color.rgb(0, 0, 255, 0.4);
    public static final Color COLOR_END        = Color.rgb(0, 255, 0, 0.4);
    public static final Color COLOR_SOLUTION   = Color.rgb(255, 255, 0, 0.4);
    public static final Color COLOR_PLAYER    = Color.rgb(255, 0, 0, 0.4);

    private boolean showingSolution;

    public DisplayableMaze(Maze maze, Solution solution) {
        super(maze);
        showingSolution = false;

        ArrayList<AState> sol = solution.getSolutionPath();
        for (AState state : sol){
            Position statePosition = ((MazeState)state).getStatePosition();
            this.maze[statePosition.getRowIndex()][statePosition.getColumnIndex()] = TILE_SOLUTION;
        }

        this.maze[this.getStartPosition().getRowIndex()][this.getStartPosition().getColumnIndex()] = TILE_START;
        this.maze[this.getGoalPosition().getRowIndex()][this.getGoalPosition().getColumnIndex()] = TILE_END;
    }

    public int getTile(int col, int row) {
        if (!this.showingSolution && maze[row][col] == TILE_SOLUTION) return TILE_FLOOR;
        return maze[row][col];
    }

    public Color getMiniMapTileColor(int col, int row) {
        int tile = getTile(col, row);
        switch (tile) {
            case TILE_FLOOR:
                return COLOR_FLOOR;
            case TILE_WALL:
                return COLOR_WALL;
            case TILE_START:
                return COLOR_START;
            case TILE_END:
                return COLOR_END;
            case TILE_SOLUTION:
                return COLOR_SOLUTION;
        }

        return COLOR_FLOOR;
    }

    public void toggleSolution() {
        this.showingSolution = !showingSolution;
    }

    public int getWidth(){ return maze[0].length; }
    public int getHeight(){ return maze.length; }


}
