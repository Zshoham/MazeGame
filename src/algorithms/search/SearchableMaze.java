package algorithms.search;

import algorithms.mazeGenerators.Maze;

import java.util.LinkedList;

/**
 * Represents a maze search problem.
 */
public class SearchableMaze extends Maze implements ISearchable<MazeState> {

    private static IDistanceHeuristic mazeHeuristic = new MazeDistanceHeuristic();

    private MazeState startState;
    private MazeState goalState;

    /**
     * Constructs a new searchable maze from a maze.
     * @param maze
     */
    public SearchableMaze(Maze maze){
        super(maze);
        startState = new MazeState(this.getStartPosition());
        goalState = new MazeState(this.getGoalPosition());
    }

    @Override
    public LinkedList<AState> getAllPossibleStates(MazeState origin) {
        LinkedList<AState> open = new LinkedList<>();
        int oRow = origin.getStatePosition().getRowIndex();
        int oCol = origin.getStatePosition().getColumnIndex();

        if (oRow + 1 < rows && this.maze[oRow + 1][oCol] == 0) open.add(new MazeState(oRow + 1, oCol));
        if (oRow - 1 >= 0 && this.maze[oRow - 1][oCol] == 0) open.add(new MazeState(oRow - 1, oCol));
        if (oCol + 1 < cols && this.maze[oRow][oCol + 1] == 0) open.add(new MazeState(oRow, oCol + 1));
        if (oCol - 1 >= 0 && this.maze[oRow][oCol - 1] == 0) open.add(new MazeState(oRow, oCol - 1));

        return open;
    }

    @Override
    public IDistanceHeuristic getDistanceHeuristic() {
        return mazeHeuristic;
    }

    @Override
    public MazeState getStartState() {
        return this.startState;
    }

    @Override
    public MazeState getGoalState() {
        return this.goalState;
    }

}
