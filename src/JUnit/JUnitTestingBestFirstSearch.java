import algorithms.mazeGenerators.*;
import algorithms.search.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JUnitTestingBestFirstSearch {

    MyMazeGenerator generator;
    SearchableMaze bigMaze;
    SearchableMaze smallMaze;

    @BeforeEach
    void setUp() {
        generator = new MyMazeGenerator();
        Maze big = generator.generate(1000, 1000);
        bigMaze = new SearchableMaze(big);
        int[][] small = new int[][] {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 1, 0, 1, 1, 1, 1, 1, 1, 1, 0 },
                { 1, 0, 0, 1, 1, 1, 1, 1, 1, 0 },
                { 1, 1, 0, 1, 0, 0, 0, 1, 1, 0 },
                { 1, 1, 0, 1, 0, 1, 0, 1, 1, 0 },
                { 1, 0, 0, 1, 0, 1, 0, 1, 1, 0 },
                { 1, 0, 1, 0, 0, 1, 0, 1, 1, 0 },
                { 1, 0, 0, 0, 1, 1, 0, 1, 1, 0 },
                { 1, 1, 1, 1, 1, 1, 0, 1, 1, 0 },
                { 1, 1, 1, 1, 1, 1, 0, 0, 0, 0 }
        };
        smallMaze = new SearchableMaze(new Maze(small, new Position(0,0), new Position(9,9)));
    }

    @Test
    void solve() {
        long timer = System.currentTimeMillis();
        ISearchingAlgorithm Solver = new BestFirstSearch();
        Solution bigSolution = Solver.solve(bigMaze);
        bigMaze.print();
        assert (timer - System.currentTimeMillis() < 60 * 1000);

        assert (bigSolution.getSolutionPath().size() <= Solver.getNumberOfNodesEvaluated());
        assert (bigSolution.getSolutionPath().size() > bigMaze.getStartPosition().getDistance(bigMaze.getGoalPosition()));

        Solution smallSolution = Solver.solve(smallMaze);
        smallMaze.printSolution(smallSolution);

        assert (smallSolution.getSolutionPath().size() <= Solver.getNumberOfNodesEvaluated());
        assert (smallSolution.getSolutionPath().size() > smallMaze.getStartPosition().getDistance(smallMaze.getGoalPosition()));
        assert (smallSolution.getSolutionPath().size() == 18);

    }

    @Test
    void getNumberOfNodesEvaluated() {
        ISearchingAlgorithm Solver = new BestFirstSearch();
        Solution smallSolution = Solver.solve(smallMaze);
        smallMaze.printSolution(smallSolution);

        assert (Solver.getNumberOfNodesEvaluated() <= 44);
    }

    @Test
    void getName() {
        assertEquals((new BestFirstSearch()).getName(), "Best First Search");
    }
}