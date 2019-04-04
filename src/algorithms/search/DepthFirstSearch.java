package algorithms.search;

import java.util.LinkedList;

public class DepthFirstSearch extends ASearchingAlgorithm {

    private LinkedList<AState> evalList;

    public DepthFirstSearch() {
        name = "Depth First Search";
        this.evalList = new LinkedList<>();
    }

    @Override
    public Solution solve(ISearchable searchable) {
        searchable.getStartState().parent = null;
        this.numStatesEvaluated = 0;
        dfsVisit(searchable, searchable.getStartState());

        return new Solution(searchable.getGoalState());
    }

    private void dfsVisit(ISearchable domain, AState current) {
        this.numStatesEvaluated++;
        if (current.equals(domain.getGoalState())) {
            domain.getGoalState().parent = current.parent;
            return;
        }

        evalList.add(current);

        LinkedList<AState> neighbors = domain.getAllPossibleStates(current);
        for(AState state : neighbors) {
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
