package algorithms.search;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    protected static String name;

    protected Solution solution;
    protected AState currentState;
    protected int numStatesEvaluated;

    @Override
    public Solution solve(ISearchable searchable) {
        return null;
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return numStatesEvaluated;
    }
}
