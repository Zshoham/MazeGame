package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.io.Serializable;

/**
 * Represents a state in a maze search problem.
 */
public class MazeState extends AState implements Serializable {

    private Position position;

    public MazeState(Position position) {
        this.position = new Position(position);
    }

    public MazeState(int row, int col) {
        this.position = new Position(row, col);
    }

    public Position getStatePosition() {
        return position;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other instanceof MazeState) {
            return ((MazeState) other).getStatePosition().equals(this.getStatePosition());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.position.hashCode();
    }
}
