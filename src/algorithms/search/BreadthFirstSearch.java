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
    protected int getCost(AState destination, ISearchable heuristic) {
        return 1;
    }
}
