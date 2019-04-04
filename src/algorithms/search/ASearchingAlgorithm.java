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
}
