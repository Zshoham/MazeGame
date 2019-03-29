package algorithms.search;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    protected static String name;

    protected Solution solution;
    protected AState currentState;

    @Override
    public Solution solve(ISearchable searchable) {
        return null;
    }
}
