package algorithms.search;

public class BreadthFirstSearch extends ABreadthFirstSearch {

    private static String name = "Breadth First Search";

    @Override
    protected int getCost(AState destination, ISearchable domain) {
        return 1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return 0;
    }
}
