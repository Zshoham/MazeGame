package algorithms.search;

/**
 * The basic Breadth First Search Algorithm.
 * It assigns a cost of 0 to all its states
 * thus picking at random which state should be visited next.
 */
public class BreadthFirstSearch extends ABreadthFirstSearch {


    public BreadthFirstSearch() {
        name = "Breadth First Search";
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    protected int getCost(AState destination, ISearchable heuristic) {
        return 0;
    }
}
