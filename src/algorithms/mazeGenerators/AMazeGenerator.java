package algorithms.mazeGenerators;

import java.util.Random;


public abstract class AMazeGenerator implements IMazeGenerator{

    protected Position start;
    protected Position goal;


    public AMazeGenerator()
    {
        this.start = null;
        this.goal = null;
    }

    /**
     * Picks a starting position for the maze.
     * Sets {@link AMazeGenerator#start} to the chosen position.
     * @param rows the number of rows in the maze.
     * @param cols the number of columns in the maze.
     */
    protected void pickStart(int rows, int cols)
    {
        while (start == null || start.equals(goal))
        {
            start = this.chooseEdgePosition(rows, cols);
            if (goal != null && (goal.getColumnIndex() == start.getColumnIndex() || goal.getRowIndex() == start.getRowIndex()))
                start = null;
        }
    }

    /**
     * Picks a goal position for the maze.
     * Sets {@link AMazeGenerator#goal} to the chosen position.
     * @param rows the number of rows in the maze.
     * @param cols the number of columns in the maze.
     */
    protected void pickGoal(int rows, int cols)
    {
        while (goal == null || goal.equals(start))
        {
            goal = this.chooseEdgePosition(rows,cols);
            if (start != null && (goal.getColumnIndex() == start.getColumnIndex() || goal.getRowIndex() == start.getRowIndex()))
                goal = null;
        }
    }

    /**
     * Resets the algorithm making it ready to generate another maze.
     */
    protected void reset()
    {
        this.start = null;
        this.goal = null;
    }

    /**
     * Chooses a cell on one of the edges of the maze.
     * @param rows the number of rows in the maze.
     * @param cols the number of columns in the maze.
     * @return the chosen position.
     */
    protected Position chooseEdgePosition(int rows, int cols)
    {
        Random random = new Random(System.nanoTime());
        int edge = random.nextInt(4);
        int edgeOffset;
        Position res = null;
        switch (edge){
            case 0:
                edgeOffset = random.nextInt(cols);
                res = new Position(0, edgeOffset);
                break;
            case 1:
                edgeOffset = random.nextInt(cols);
                res = new Position(rows - 1, edgeOffset);
                break;
            case 2:
                edgeOffset = random.nextInt(rows);
                res = new Position(edgeOffset, 0);
                break;
            case 3:
                edgeOffset = random.nextInt(rows);
                res = new Position(edgeOffset, cols - 1);
                break;
        }

        return res;
    }


    public long measureAlgorithmTimeMillis(int rows, int cols){
        long sTime = System.currentTimeMillis();
        generate(rows, cols);
        long eTime = System.currentTimeMillis();
        return eTime-sTime;
    }
}
