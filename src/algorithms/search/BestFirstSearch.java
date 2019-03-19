package algorithms.search;

public class BestFirstSearch extends ABreadthFirstSearch {

    private static String name = "Best First Search";

    @Override
    protected int getCost(AState destination, ISearchable domain) {
        //TODO: if cost is added to AState then add the cost of the current state to the calculation.
        return domain.getHeuristic().getDistance(destination, domain.getGoalState());
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
