package algorithms.search;

import algorithms.mazeGenerators.Maze;

public class SearchableMaze extends Maze implements ISearchable<MazeState> {

    private static ISearchHeuristic mazeHeuristic = new MazeSearchHeuristic();

    private Maze maze;

    public SearchableMaze(Maze maze){
        this.maze = maze;
    }

    @Override
    public MazeState[] getAllPossibleStates(MazeState origin) {
        return new MazeState[0];
    }

    @Override
    public ISearchHeuristic getHeuristic() {
        return mazeHeuristic;
    }

    @Override
    public MazeState getGoalState() {
        return new MazeState(maze.getGoalPosition());
    }
}
