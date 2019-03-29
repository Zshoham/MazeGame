package algorithms.search;

public interface ISearchable<T extends AState> {

    T[] getAllPossibleStates(T origin);

    ISearchHeuristic getHeuristic();

    T getStartState();

    T getGoalState();

    //TODO: add ability to iterate all the states.

}
