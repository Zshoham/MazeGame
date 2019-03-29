package algorithms.search;

public class DepthFirstSearch extends ASearchingAlgorithm {

    private static int WHITE    =   1;
    private static int BLACK    =   2;
    private static int GREY     =   3;


    public DepthFirstSearch() {
        name = "Depth First Search";
    }

    @Override
    public Solution solve(ISearchable searchable) {
        /*
        Pseudo Code:
            for each State in searchable set color to WHITE
            set searchable.getStartState.parent to null
            dfsVisit(searchable.getStartState)
            return Solution(searchable.getStartState)
         */

        return new Solution(searchable.getGoalState());
    }

    public boolean dfsVisit(ISearchable domain, AState current) {
        /*
        Pseudo Code:
            if current = domain.getStartState
                return true
            set color of current to GREY.
            for each state s in domain.getAllPossibleStates(current)
                if s is WHITE call dfsVisit(domain, s)

            set color of current to BLACK.
            return false
         */

         return false;
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
