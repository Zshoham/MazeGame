package algorithms.mazeGenerators;

import IO.Serializer;
import algorithms.search.MazeState;
import algorithms.search.Solution;

/**
 * Wrapper for the maze data.
 * it holds the the array describing the maze
 * and the positions of the start and the goal of the maze.
 */
public class Maze {

    private static final boolean DEBUG = false;

    public static final int HEADER_LENGTH = 14;

    protected int[][] maze;
    protected int rows;
    protected int cols;
    private Position startPosition;
    private Position goalPosition;

    /**
     * Constructs a new maze.
     *
     * @param maze          the array describing the maze.
     * @param startPosition the starting position in the maze.
     * @param goalPosition  the goal position of the maze.
     */
    public Maze(int[][] maze, Position startPosition, Position goalPosition) {
        this.maze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.startPosition = startPosition;
        this.goalPosition = goalPosition;
    }

    /**
     * Constructs a copy of a maze.
     *
     * @param other the maze to be copied.
     */
    public Maze(Maze other) {
        this.maze = new int[other.maze.length][other.maze[0].length];
        for (int x = 0; x < this.maze.length; x++) {
            System.arraycopy(other.maze[x], 0, this.maze[x], 0, this.maze[0].length);
        }
        this.rows = other.rows;
        this.cols = other.cols;
        this.startPosition = new Position(other.startPosition);
        this.goalPosition = new Position(other.goalPosition);
    }

    /**
     * Deserialize's the mazeData and constructs a maze accordingly. <br/>
     * The data format is described in the serialization method {@link Maze#toByteArray()}
     * @param mazeData the serialized maze data.
     * @see Maze#toByteArray()
     */
    public Maze(byte[] mazeData) {
        int pointer = 0;
        this.cols = Serializer.readShort(pointer, mazeData);
        pointer += 2;
        this.rows = Serializer.readShort(pointer, mazeData);
        pointer += 2;
        this.startPosition = deserializeEdgePosition(pointer, mazeData);
        pointer += 3;
        this.goalPosition = deserializeEdgePosition(pointer, mazeData);
        pointer += 3;

       int size = Serializer.readInt(pointer, mazeData);
       pointer += 4;

        assert (size != HEADER_LENGTH + (rows * cols)); //TODO: change to an exception or return null ?

        this.maze = new int[rows][cols];
        for (int y = 0; y < this.rows; y++) {
            for (int x = 0; x < this.cols; x++) {
                assert (mazeData[pointer + (x + y * cols)] > 1 || mazeData[pointer + (x + y * cols)] < 0); //TODO: change to an exception or return null ?
                this.maze[y][x] = mazeData[pointer + (x + y * cols)];
            }
        }
    }

    private Position deserializeEdgePosition(int pointer, byte[] data){
        int x = 0, y = 0;

        switch (data[pointer]) {
            case 1:
                pointer++;
                y = Serializer.readShort(pointer, data);
                break;
            case 2:
                x = cols - 1;
                pointer++;
                y = Serializer.readShort(pointer, data);
                break;
            case 3:
                pointer++;
                x = Serializer.readShort(pointer, data);
                break;
            case 4:
                y = rows - 1;
                pointer++;
                x = Serializer.readShort(pointer, data);
                break;
        }

        return new Position(y, x);
    }

    /**
     * @return the starting position of the maze.
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * @return the goal position in the maze.
     */
    public Position getGoalPosition() {
        return goalPosition;
    }

    //TODO: depending on how the size should be handled, maybe make size the first part of the header.
    /**
     * Serializes the maze data into a byte array representation according to the following specification:
     *
     * |--------------------------------------------------| <br/>
     * |Header:                                           | <br/>
     * | * 2 bytes for width of the maze (short).         | <br/>
     * | * 2 bytes for the height of the maze (short).    | <br/>
     * | * 3 bytes for start position (1 byte + 1 short ).| <br/>
     * | * 3 bytes for goal position. (1 byte + 1 short ).| <br/>
     * | * 4 bytes for size (int).                        | <br/>
     * | -------------------------------------------------- <br/>
     * |Data:                                             | <br/>
     * | the maze stretched into a long array starting at | <br/>
     * | the top left corner and going right over all the | <br/>
     * | columns then doing the same for the next row     | <br/>
     * | until the final row is serialized.               | <br/>
     * |--------------------------------------------------| <br/>
     *
     * note: each of the edge positions is serialized in the following manner:  <br/>
     *  * 1 byte determines the wall the position belongs to.                   <br/>
     *      - 1 is for the left wall.                                           <br/>
     *      - 2 is for the right wall.                                          <br/>
     *      - 3 is for the top wall.                                            <br/>
     *      - 4 is for the bottom wall.                                         <br/>
     *  * 2 bytes (short) to determine the offset of the position on the wall   <br/>
     *      - positive offset is right on the bottom and top walls.             <br/>
     *      - positive offset is down on the right and left walls.              <br/>
     *
     * @return the serialized byte array.
     */
    public byte[] toByteArray() {
        int size = HEADER_LENGTH + (this.rows * this.cols);

        byte[] res = new byte[size];
        int pointer = 0;

        //write the header.
        pointer = Serializer.write((short) this.cols, pointer, res);
        pointer = Serializer.write((short) this.rows, pointer, res);
        pointer = serializeEdgePosition(this.startPosition, pointer, res);
        pointer = serializeEdgePosition(this.goalPosition, pointer, res);

        //write the size.
        pointer = Serializer.write(size, pointer, res);

        //write the maze itself.
        for (int y = 0; y < this.rows; y++) {
            for (int x = 0; x < this.cols; x++) {
                res[pointer + (x + y * cols)] = (byte) this.maze[y][x];
            }
        }

        return res;
    }

    private int serializeEdgePosition(Position pos, int pointer, byte[] dest) {
        short offset = 0;
        if (pos.getColumnIndex() == 0) {
            dest[pointer++] = 1;
            offset = (short) pos.getRowIndex();
        }
        else if (pos.getColumnIndex() == this.cols - 1) {
            dest[pointer++] = 2;
            offset = (short) pos.getRowIndex();
        }
        else if (pos.getRowIndex() == 0) {
            dest[pointer++] = 3;
            offset = (short) pos.getColumnIndex();
        }
        else if (pos.getRowIndex() == this.rows - 1) {
            dest[pointer++] = 4;
            offset = (short) pos.getColumnIndex();
        }
        pointer = Serializer.write(offset, pointer, dest);

        return pointer;
    }

    /**
     * Prints the maze to the console. <br/>
     * The start position is the letter S. <br/>
     * The goal position is the letter E. <br/>
     * 1 Represents a wall, and 0 represents an empty cell.
     */
    public void print() {
        if (DEBUG) {
            beautifyPrint();
            return;
        }

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (startPosition.equals(new Position(y, x))) System.out.print("S ");
                else if (goalPosition.equals(new Position(y, x))) System.out.print("E ");
                else System.out.print(maze[y][x] + " ");
            }
            System.out.println();
        }
    }

    private void beautifyPrint() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == this.startPosition.getRowIndex() && j == this.startPosition.getColumnIndex()) {//startPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (i == this.goalPosition.getRowIndex() && j == this.goalPosition.getColumnIndex()) {//goalPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (maze[i][j] == 1) System.out.print(" " + "\u001B[45m" + " ");
                else System.out.print(" " + "\u001B[107m" + " ");
            }
            System.out.println(" " + "\u001B[107m");
        }
    }

    public void printSolution(Solution sol) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (i == this.startPosition.getRowIndex() && j == this.startPosition.getColumnIndex()) {//startPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (i == this.goalPosition.getRowIndex() && j == this.goalPosition.getColumnIndex()) {//goalPosition
                    System.out.print(" " + "\u001B[46m" + " ");
                } else if (maze[i][j] == 1) System.out.print(" " + "\u001B[45m" + " ");
                else if (sol.getSolutionPath().contains(new MazeState(i, j)))
                    System.out.print(" " + "\u001B[41m" + " ");
                else System.out.print(" " + "\u001B[107m" + " ");
            }
            System.out.println(" " + "\u001B[107m");
        }
    }
}