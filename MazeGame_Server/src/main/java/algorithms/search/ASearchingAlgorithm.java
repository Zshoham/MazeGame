package algorithms.search;

/**
 * An abstract searching algorithm.
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    protected static String name;

    protected AState currentState;
    protected int numStatesEvaluated;

    @Override
    public int getNumberOfNodesEvaluated() {
        return numStatesEvaluated;
    }

    /**
     * Resets the algorithm making it ready to be applied to another problem.
     */
    protected void reset() {
        this.currentState = null;
        this.numStatesEvaluated = 0;
    }
}
