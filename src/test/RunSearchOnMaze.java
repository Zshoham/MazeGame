package test;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import java.util.ArrayList;

public class RunSearchOnMaze {
    public static void main(String[] args) {
        IMazeGenerator mg = new MyMazeGenerator();
        //IMazeGenerator mg = new SimpleMazeGenerator();
        Maze maze = mg.generate(100, 100);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        Solution sol;
        sol = solveProblem(searchableMaze, new BreadthFirstSearch());
        searchableMaze.printSolution(sol);
        sol = solveProblem(searchableMaze, new DepthFirstSearch());
        searchableMaze.printSolution(sol);
        sol = solveProblem(searchableMaze, new BestFirstSearch());
        searchableMaze.printSolution(sol);

    }
    private static Solution solveProblem(ISearchable domain, ISearchingAlgorithm searcher) {
        //Solve a searching problem with a searcher
        Solution solution = searcher.solve(domain);
        System.out.println(String.format("'%s' algorithm - nodes evaluated: %s", searcher.getName(), searcher.getNumberOfNodesEvaluated()));
        //Printing Solution Path
        System.out.println("Solution path:");
        ArrayList<AState> solutionPath = solution.getSolutionPath();
        for (int i = 0; i < solutionPath.size(); i++) {
            System.out.println(String.format("%s. %s",i,solutionPath.get(i)));
        }
        return solution;
    }
}