package algorithms.search;

import java.util.LinkedList;

public interface ISearchable<T extends AState> {

    LinkedList<AState> getAllPossibleStates(T origin);

    ISearchHeuristic getHeuristic();

    T getStartState();

    T getGoalState();

    //TODO: add ability to iterate all the states.
}
