package algorithms.search;

public interface ISearchHeuristic<T extends AState> {

    int getDistance(T first, T second);
}
