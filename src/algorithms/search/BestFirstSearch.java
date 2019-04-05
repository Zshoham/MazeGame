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
    protected double getCost(AState destination, ISearchable domain) {
        int distance = 1;
        //if the euclidean distance is greater than 1 its a diagonal jump then the distance should be 2;
        if (domain.getDistanceHeuristic().getDistance(this.currentState, destination) > 1)
            distance = 2;

        return domain.getDistanceHeuristic().getDistance(destination, domain.getGoalState()) //heuristic cost
                + currentState.cost //parent cost
                + distance; // travel cost
    }

    @Override
    public String getName() {
        return name;
    }

}
