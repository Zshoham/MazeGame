package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Wrapper for the solution of a search problem. <br/>
 * contains a list representing the path from the start state
 * to the goal state.
 */
public class Solution implements Serializable {

    private ArrayList<AState> path;
    private String algorithm;

    public Solution(AState goalState, String algorithm) {
        this.algorithm = algorithm;
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

    @Override
    public String toString() {
        return "Solved using - " + algorithm;
    }
}
