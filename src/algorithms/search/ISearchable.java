package algorithms.search;

import java.util.LinkedList;

/**
 * Interface describing the common properties of all search problems.
 *
 * @param <T> The type of states the search problem deals with.
 */
public interface ISearchable<T extends AState> {

    /**
     * @return all the neighbors of a given state.
     */
    LinkedList<AState> getAllPossibleStates(T origin);

    /**
     * @return the distance heuristic that should be used to for this problem.
     */
    IDistanceHeuristic getDistanceHeuristic();

    /**
     * @return the starting state of the problem.
     */
    T getStartState();

    /**
     * @return the destination state of the problem.
     */
    T getGoalState();
}
