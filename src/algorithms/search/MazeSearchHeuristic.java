package algorithms.search;

public class MazeSearchHeuristic implements ISearchHeuristic<MazeState> {

    @Override
    public int getDistance(MazeState first, MazeState second) {
        int fx = first.getStatePosition().getColumnIndex();
        int fy = first.getStatePosition().getRowIndex();
        int sx = second.getStatePosition().getColumnIndex();
        int sy = second.getStatePosition().getRowIndex();
        int dx = Math.abs(fx - sx);
        int dy = Math.abs(fy - sy);
        return (int) Math.sqrt(dx*dx + dy*dy);
    }
}
