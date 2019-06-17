package algorithms.search;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/**
 * The Depth First Search Algorithm.
 */
public class DepthFirstSearch extends ASearchingAlgorithm {

    private HashSet<AState> evalSet;

    public DepthFirstSearch() {
        name = "Depth First Search";
        this.evalSet = new HashSet<>();
    }

    @Override
    public Solution solve(ISearchable domain) {
        reset();

        domain.getStartState().parent = null;
        this.numStatesEvaluated = 0;

        this.currentState = domain.getStartState();
        Stack<AState> stack = new Stack<>();
        stack.push(this.currentState);
        while (!stack.isEmpty()) {
            if (stack.peek().equals(domain.getGoalState())) {
                domain.getGoalState().parent = stack.pop().parent;
                break;
            }
            this.currentState = stack.pop();
            if (!evalSet.contains(this.currentState)) {
                evalSet.add(this.currentState);
                LinkedList<AState> neighbors = domain.getAllPossibleStates(this.currentState);
                for (AState s : neighbors) {
                    this.numStatesEvaluated++;
                    s.parent = this.currentState;
                    stack.push(s);
                }
            }
        }

        return new Solution(domain.getGoalState());
    }

    @Override
    public String getName() {
        return name;
    }
}
