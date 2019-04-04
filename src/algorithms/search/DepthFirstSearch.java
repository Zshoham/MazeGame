package algorithms.search;

import java.util.LinkedList;

/**
 * The Depth First Search Algorithm.
 */
public class DepthFirstSearch extends ASearchingAlgorithm {

    private LinkedList<AState> evalList;

    public DepthFirstSearch() {
        name = "Depth First Search";
        this.evalList = new LinkedList<>();
    }

    @Override
    public Solution solve(ISearchable domain) {
        domain.getStartState().parent = null;
        this.numStatesEvaluated = 0;
        dfsVisit(domain, domain.getStartState());

        return new Solution(domain.getGoalState());
    }

    /**
     * The working function of the algorithm,
     * it will recursively visit the neighbors of current
     * until it reaches the goal or all options are exhausted.
     *
     * @param domain  the searching problem.
     * @param current the current node the algorithm is using
     *                (should be called with start as its current state)
     */
    private void dfsVisit(ISearchable domain, AState current) {
        this.numStatesEvaluated++;
        if (current.equals(domain.getGoalState())) {
            domain.getGoalState().parent = current.parent;
            return;
        }

        evalList.add(current);

        LinkedList<AState> neighbors = domain.getAllPossibleStates(current);
        for (AState state : neighbors) {
            if (!evalList.contains(state)) {
                state.parent = current;
                dfsVisit(domain, state);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
