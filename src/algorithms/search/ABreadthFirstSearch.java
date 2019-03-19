package algorithms.search;

public abstract class ABreadthFirstSearch extends ASearchingAlgorithm {

    protected abstract int getCost(AState destination, ISearchable domain);

    @Override
    public Solution solve(ISearchable searchable) {
        return new Solution();
    }
}
