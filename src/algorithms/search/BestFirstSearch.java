package algorithms.search;

/**
 * A searching algorithm based on the Best First Search algorithm
 * that uses a heuristic to calculate the cost of visiting states.
 */
public class BestFirstSearch extends ABreadthFirstSearch {


    public BestFirstSearch() {
        name = "Best First Search";
    }

    @Override
    protected int getCost(AState destination, ISearchable domain) {
        return domain.getDistanceHeuristic().getDistance(destination, domain.getGoalState()); //heuristic cost
                //+ currentState.cost //parent cost
                //+ 1; // travel cost
    }

    @Override
    public String getName() {
        return name;
    }

}
