package algorithms.mazeGenerators;
import java.util.ArrayList;
import java.util.Random;

/**
 * Implementation of a maze generator algorithm.
 *
 * <p>Generates a maze based on a modified version of the Recursive Division Algorithm.
 * The start and goal positions are chosen at random once the maze generation is complete.</p>
 */
public class MyMazeGenerator extends AMazeGenerator {

    /**
     * Constant indicating if the walls should be put on even or odd indexes. <br/>
     * If wallPosConstant = 1 walls will be placed in odd indexes, otherwise
     * walls will be placed in even indexes.
     */
    private int wallPosConstant;

    /**
     * Constant indicating if the wall clearings should be put on even or odd indexes. <br/>
     * The value of passPosConstant is always the binary inverse of {@link MyMazeGenerator#wallPosConstant}
     */
    private int passPosConstant;

    public MyMazeGenerator()
    {
        Random random = new Random(System.nanoTime());
        wallPosConstant = random.nextInt(2);
        passPosConstant = 1 - wallPosConstant;
    }

    /**
     * @param rows the number of rows the maze will have.
     * @param cols the number of columns the maze will have.
     * @return a maze generated using the Recursive Division Algorithm.
     */
    @Override
    public Maze generate(int rows, int cols) {
        reset();
        int[][] aMaze = new int[rows][cols];

        //initialize the mze to be empty.
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                aMaze[y][x] = 0;
            }
        }

        makeMaze(aMaze, 0, cols - 1, 0, rows - 1);

        pickStart(rows, cols);
        pickGoal(rows, cols);

        return new Maze(aMaze, this.start, this.goal);
    }

    /**
     * The work function in charge of the maze generation.
     * @param maze reference to the maze being generated.
     * @param left the left most index in the maze.
     * @param right the right most index of the maze.
     * @param top the top most index of the maze.
     * @param bottom the bottom most index of the maze.
     */
    private void makeMaze(int[][] maze, int left, int right, int top, int bottom)
    {
        int width = right - left;
        int height = bottom - top;

        Random random = new Random(System.nanoTime());

        if (width > 2 && height > 2)
        {
            if (width > height) davideHorizontal(maze, left, right, top, bottom);
            else if (height > width) davideVertical(maze, left, right, top, bottom);
            else
            {
                boolean isHorizontal = random.nextBoolean();
                if (isHorizontal) davideHorizontal(maze, left, right, top, bottom);
                else davideVertical(maze, left, right, top, bottom);
            }
        }
        else if (width > 2 && height <= 2) davideHorizontal(maze, left, right, top, bottom);
        else if (width <= 2 && height > 2) davideVertical(maze, left, right, top, bottom);
    }

    /**
     * The work function in charge of generating walls for mazes
     * that are Vertical (width < height), the function adds horizontal walls.
     * @param maze maze reference to the maze being generated.
     * @param left the left most index in the maze.
     * @param right the right most index of the maze.
     * @param top the top most index of the maze.
     * @param bottom the bottom most index of the maze.
     */
    private void davideVertical(int[][] maze, int left, int right, int top, int bottom)
    {
        Random random = new Random(System.nanoTime());

        int divisionPoint = chooseDivision(top, bottom);

        for (int x = left; x < right + 1; x++) {
            maze[divisionPoint][x] = 1;
        }

        int clearingPoint = chooseClearing(left, right);

        maze[divisionPoint][clearingPoint] = 0;

        if (divisionPoint > 0) makeMaze(maze, left, right, top, divisionPoint - 1);
        if (divisionPoint < bottom) makeMaze(maze, left, right, divisionPoint + 1, bottom);
    }

    /**
     * The work function in charge of generating walls for mazes
     * that are Horizontal (height < width), the function adds vertical walls.
     * @param maze maze reference to the maze being generated.
     * @param left the left most index in the maze.
     * @param right the right most index of the maze.
     * @param top the top most index of the maze.
     * @param bottom the bottom most index of the maze.
     */
    private void davideHorizontal(int[][] maze, int left, int right, int top, int bottom)
    {
        Random random = new Random(System.nanoTime());

        int divisionPoint = chooseDivision(left, right);

        for (int y = top; y < bottom + 1; y++) {
            maze[y][divisionPoint] = 1;
        }

        int clearingPoint = chooseClearing(top, bottom);

        maze[clearingPoint][divisionPoint] = 0;

        if (divisionPoint > 0) makeMaze(maze, left, divisionPoint - 1, top, bottom);
        if (divisionPoint < right) makeMaze(maze, divisionPoint + 1, right, top, bottom);
    }

    /**
     * Chooses a random index between min (inclusive) and max (inclusive)
     * where a wall will be added in the maze.
     * The function will only consider indexes that have the following property: <br/>
     * index = 2n + {@link MyMazeGenerator#wallPosConstant}, where n is a positive integer.
     * @param min the lower limit of the random generation.
     * @param max the upper limit of the random generation.
     * @return an integer representing the index where a wall should be placed.
     */
    private int chooseDivision(int min, int max)
    {
        ArrayList<Integer> range = new ArrayList<>();
        Random random = new Random(System.nanoTime());

        for (int i = min; i <= max; i++) {
            if (i % 2 == wallPosConstant) range.add(i);
        }

        return range.get(random.nextInt(range.size()));
    }

    /**
     * Chooses a random index between min (inclusive) and max (inclusive)
     * where a clearing will be placed a wall.
     * The function will only consider indexes that have the following property: <br/>
     * index = 2n + {@link MyMazeGenerator#passPosConstant}, where n is a positive integer.
     * @param min the lower limit of the random generation.
     * @param max the upper limit of the random generation.
     * @return an integer representing the index where a wall should be placed.
     */
    private int chooseClearing(int min, int max)
    {
        ArrayList<Integer> range = new ArrayList<>();
        Random random = new Random(System.nanoTime());

        for (int i = min; i <= max; i++) {
            if (i % 2 == passPosConstant) range.add(i);
        }

        return range.get(random.nextInt(range.size()));
    }

    @Override
    protected void reset() {
        super.reset();
        Random random = new Random(System.nanoTime());
        this.wallPosConstant = random.nextInt(2);
        this.passPosConstant = 1 - wallPosConstant;
    }

    @Override
    protected Position chooseEdgePosition(int rows, int cols) {

        Random random = new Random(System.nanoTime());
        int edge = random.nextInt(4);
        int edgeOffset;
        Position res = null;

        switch (edge) {
            case 0:
                edgeOffset = chooseClearing(0, cols);
                res = new Position(0, edgeOffset);
                break;
            case 1:
                edgeOffset = chooseClearing(0, cols);
                res = new Position(rows - 1, edgeOffset);
                break;
            case 2:
                edgeOffset = chooseClearing(0, rows);
                res = new Position(edgeOffset, 0);
                break;
            case 3:
                edgeOffset = chooseClearing(0, rows);
                res = new Position(edgeOffset, cols - 1);
                break;
        }

        return res;
    }
}
