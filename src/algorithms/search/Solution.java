package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

/**
 * Wrapper for the solution of a search problem. <br/>
 * contains a list representing the path from the start state
 * to the goal state.
 */
public class Solution {

    private ArrayList<AState> path;

    public Solution(AState goalState) {
        path = new ArrayList<>();
        AState itr = goalState;
        while (itr.parent != null) {
            path.add(0, itr);
            itr = itr.parent;
        }
    }

    /**
     * @return the path from the start state to the goal state.
     */
    public ArrayList<AState> getSolutionPath() {
        return path;
    }

    public boolean contains(int i, int j) {
        AState state = new MazeState(new Position(i, j));
        return this.path.contains(state);
    }
}
