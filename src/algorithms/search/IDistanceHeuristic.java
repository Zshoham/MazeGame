package algorithms.search;

/**
 * A heuristic function used to calculate the distance between two states in
 * a searching problem.
 *
 * @param <T> The type of states the heuristic applies to.
 */
public interface IDistanceHeuristic<T extends AState> {

    /**
     * @return the heuristic distance between first and second.
     */
    int getDistance(T first, T second);
}
