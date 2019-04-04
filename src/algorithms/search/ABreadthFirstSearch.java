package algorithms.search;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class ABreadthFirstSearch extends ASearchingAlgorithm {

    private PriorityQueue<AState> openQueue;
    private LinkedList<AState> closedList;

    public ABreadthFirstSearch()
    {
        openQueue = new PriorityQueue<>(new AState.StateComparator());
        closedList = new LinkedList<>();
    }

    @Override
    public Solution solve(ISearchable searchable) {
        /*
        Pseudo Code:
            init openQueue with StateCostComparator
            enqueue searchable.startState into openQueue
            for each state in searchable
                set cost infinity
                set color to WHITE
            set cost of searchable.getStartState to 0
            set color of searchable.getStartState to GREY

            while openQueue is not empty
                this.currentState = openQueue.poll()
                if this.currentState = searchable.getGoalState
                    break

                openQueue.Remove();
                this.currentState.color = BLACK

                for each state s in domain.getAllPossibleStates(this.currentState)
                    if s is not BLACK
                        newCost = getCost(s, searchable.getHeuristic)

                        if s is WHITE enqueue s into openQueue and set color to GREY
                        else if newCost >= s.cost continue

                        s.parent = this.currentState
                        s.cost = newCost

            return solution(searchable.getGoalState)
         */

        searchable.getStartState().cost = 0;
        openQueue.add(searchable.getStartState());

        while (!openQueue.isEmpty()) {
            this.currentState = openQueue.poll();
            if (this.currentState.equals(searchable.getGoalState())) {
                searchable.getGoalState().parent = currentState.parent;
                break;
            }

            openQueue.remove();
            closedList.add(this.currentState);
            this.numStatesEvaluated++;

            LinkedList<AState> neighbors = searchable.getAllPossibleStates(this.currentState);
            for (AState state : neighbors) {
                if (!closedList.contains(state)) {
                    int newCost = getCost(state, searchable);

                    if (!openQueue.contains(state)) {
                        state.parent = this.currentState;
                        state.cost = newCost;
                        openQueue.add(state);
                    }
                    else if (newCost < state.cost) {
                        //TODO: check if the priority queue heapifies for a poll.
                        state.parent = this.currentState;
                        state.cost = newCost;
                    }
                }
            }
        }


        return new Solution(searchable.getGoalState());
    }

    protected abstract int getCost(AState destination, ISearchable domain);

    @Override
    public int getNumberOfNodesEvaluated() {
        return this.numStatesEvaluated;
    }
}
