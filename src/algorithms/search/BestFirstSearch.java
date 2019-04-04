package algorithms.search;

public class BestFirstSearch extends ABreadthFirstSearch {


    public BestFirstSearch() {
        name = "Best First Search";
    }

    @Override
    protected int getCost(AState destination, ISearchable domain) {
        return domain.getHeuristic().getDistance(destination, domain.getGoalState()) //heuristic cost
                + currentState.cost //actual cost of parent
                + 1; // travel cost
    }

    @Override
    public String getName() {
        return name;
    }

}
