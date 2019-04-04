package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class Solution {

    private ArrayList<AState> path;

    public Solution(AState goalState)
    {
        path = new ArrayList<>();
        AState itr = goalState;
        while (itr.parent != null) {
            path.add(0, itr);
            itr = itr.parent;
        }
    }

    public ArrayList<AState> getSolutionPath() {
        return path;
    }

    public boolean contains(int i, int j){
        AState state = new MazeState(new Position(i, j));
        return this.path.contains(state);
    }
}
