package algorithms.mazeGenerators;

import java.util.Objects;

/**
 * Represents the position of a cell in a maze. <br/>
 * consists of the row offset and the column offset of the cell.
 */
public class Position {
    private int row;
    private int col;

    /**
     * Constructs a new position.
     *
     * @param row the row offset of the position.
     * @param col the column offset of the position.
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Constructs a copy of a position.
     *
     * @param other the position to be copied.
     */
    public Position(Position other) {
        this.row = other.row;
        this.col = other.col;
    }

    /**
     * @return the row offset of the position.
     */
    public int getRowIndex() {
        return row;
    }

    /**
     * @return the column offset of the position.
     */
    public int getColumnIndex() {
        return col;
    }

    /**
     * Finds the euclidean distance between two positions.
     *
     * @param other the position to which the distance in calculated.
     * @return the distance from this position to other.
     */
    public double getDistance(Position other) {
        int fx = this.getColumnIndex();
        int fy = this.getRowIndex();
        int sx = other.getColumnIndex();
        int sy = other.getRowIndex();
        int dx = Math.abs(fx - sx);
        int dy = Math.abs(fy - sy);
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "{" + row + "," + col + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.row, this.col);
    }
}
