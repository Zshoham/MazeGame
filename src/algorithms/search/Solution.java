package algorithms.search;

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
}
