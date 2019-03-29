package algorithms.search;

import java.util.Comparator;
import java.util.PriorityQueue;

public abstract class ABreadthFirstSearch extends ASearchingAlgorithm {

    private PriorityQueue<AState> openQueue;

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

                        if s is WHITE enqueue s into openQueue
                        else if newCost >= s.cost continue

                        s.parent = this.currentState
                        s.cost = newCost

            return solution(searchable.getGoalState)
         */

        return new Solution(searchable.getGoalState());
    }

    protected abstract int getCost(AState destination, ISearchable domain);

    private class StateCostComparator implements Comparator<AState>
    {

        @Override
        public int compare(AState o1, AState o2) {
            return Integer.compare(o2.cost, o1.cost);
        }
    }
}
