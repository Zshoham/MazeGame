package algorithms.search;

import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * An abstract breadth searching algorithm.
 */
public abstract class ABreadthFirstSearch extends ASearchingAlgorithm {

    private PriorityQueue<AState> openQueue;
    private LinkedList<AState> closedList;

    public ABreadthFirstSearch() {
        openQueue = new PriorityQueue<>(new AState.StateCostComparator());
        closedList = new LinkedList<>();
    }

    @Override
    public Solution solve(ISearchable domain) {
        domain.getStartState().cost = 0;
        openQueue.add(domain.getStartState());

        while (!openQueue.isEmpty()) {
            this.currentState = openQueue.peek();
            if (this.currentState.equals(domain.getGoalState())) {
                domain.getGoalState().parent = currentState.parent;
                break;
            }

            openQueue.poll();
            closedList.add(this.currentState);
            this.numStatesEvaluated++;

            LinkedList<AState> neighbors = domain.getAllPossibleStates(this.currentState);
            for (AState state : neighbors) {
                if (!closedList.contains(state)) {
                    int newCost = getCost(state, domain);

                    if (!openQueue.contains(state)) {
                        state.parent = this.currentState;
                        state.cost = newCost;
                        openQueue.add(state);
                    } else if (newCost < state.cost) {
                        state.parent = this.currentState;
                        state.cost = newCost;
                    }
                }
            }
        }


        return new Solution(domain.getGoalState());
    }

    /**
     * Calculates the cost that should be assigned to a state when visiting it.
     * @param destination the state being visited.
     * @param domain the searching problem.
     * @return the cost that should be assigned to the destination state.
     */
    protected abstract int getCost(AState destination, ISearchable domain);

    @Override
    public int getNumberOfNodesEvaluated() {
        return this.numStatesEvaluated;
    }
}