package algorithms.search;

public interface ISearchable<T extends AState> {

    //TODO: is it better to use generics in this way ?
    T[] getAllPossibleStates(T origin);

    ISearchHeuristic getHeuristic();

    T getGoalState();

}
