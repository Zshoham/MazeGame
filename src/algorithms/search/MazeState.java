package algorithms.search;

import algorithms.mazeGenerators.Position;

public class MazeState {
    private Position position;

    public MazeState(Position position) {
        this.position = position;
    }

    public MazeState(int row, int col) {
        this.position=new Position(row, col);
    }

    public Position getStatePosition() {
        return position;
    }
}
