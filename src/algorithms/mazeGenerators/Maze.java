package algorithms.mazeGenerators;

public class Maze {

    private int[][] maze;
    private Position startPosition;
    private Position goalPosition;

    public Maze(int[][] maze, Position startPosition, Position goalPosition) {
        this.maze = maze;
        this.startPosition = startPosition;
        this.goalPosition = goalPosition;
    }

    public Maze(Maze other)
    {
        this.maze = new int[other.maze.length][other.maze[0].length];
        for (int x = 0; x < this.maze.length; x++) {
            System.arraycopy(other.maze[x], 0, this.maze[x], 0, this.maze[0].length);
        }
        this.startPosition = new Position(other.startPosition);
        this.goalPosition = new Position(other.goalPosition);
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getGoalPosition() {
        return goalPosition;
    }

    public void print()
    {
        //TODO: is this way of printing good ?
        for (int x = 0; x < this.maze.length; x++) {
            for (int y = 0; y < this.maze[0].length; y++) {
                if(x == this.startPosition.getColumnIndex() && y == this.startPosition.getRowIndex()) System.out.print("S" + "|");
                else if(x == this.goalPosition.getColumnIndex() && y == this.goalPosition.getRowIndex()) System.out.print("E" + "|");
                else System.out.print(maze[x][y] + "|");
            }
            System.out.println();
            for (int y = 0; y < this.maze[0].length; y++) {
                System.out.print("-");
                System.out.print("|");
            }
            System.out.println();
        }
    }
}
