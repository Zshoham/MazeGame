package algorithms.search;

public class MazeSearchHeuristic implements ISearchHeuristic<MazeState> {

    @Override
    public int getDistance(MazeState first, MazeState second) {
        return (int) first.getStatePosition().getDistance(second.getStatePosition());
    }
}
