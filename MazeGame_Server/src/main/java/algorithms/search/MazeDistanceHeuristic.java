package algorithms.search;

/**
 * A distance heuristic for the distance between two positions in a maze.
 */
public class MazeDistanceHeuristic implements IDistanceHeuristic<MazeState> {

    /**
     * @return the euclidean distance between the first and second maze states.
     */
    @Override
    public double getDistance(MazeState first, MazeState second) {
        return first.getStatePosition().getDistance(second.getStatePosition());
    }
}
