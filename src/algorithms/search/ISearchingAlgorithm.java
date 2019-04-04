package algorithms.search;

/**
 * Interface describing the common functionality of all searching algorithms.
 */
public interface ISearchingAlgorithm {

    /**
     * Solves a given search problem.
     * @param domain the searching problem to be solved.
     * @return {@link Solution} to the problem.
     */
    Solution solve(ISearchable domain);

    /**
     * @return the name of the algorithm being used.
     */
    String getName();

    /**
     * @return the number of nodes or {@link AState}'s the algorithm considered.
     */
    int getNumberOfNodesEvaluated();
}
