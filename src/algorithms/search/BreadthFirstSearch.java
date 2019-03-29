package algorithms.search;

public class BreadthFirstSearch extends ABreadthFirstSearch {


    public BreadthFirstSearch() {
        name ="Breadth First Search";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return 0;
    }

    @Override
    protected int getCost(AState destination, ISearchable heuristic) {
        return 1;
    }
}
