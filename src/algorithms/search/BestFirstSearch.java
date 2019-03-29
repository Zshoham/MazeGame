package algorithms.search;

public class BestFirstSearch extends ABreadthFirstSearch {


    public BestFirstSearch() {
        name = "Best First Search";
    }

    @Override
    protected int getCost(AState destination, ISearchable domain) {
        return domain.getHeuristic().getDistance(destination, domain.getGoalState()) + currentState.cost;
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
