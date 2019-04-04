package test;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class RunSearchOnMaze {
    public static void main(String[] args) {
        IMazeGenerator mg = new MyMazeGenerator();
        //IMazeGenerator mg = new EmptyMazeGenerator();
        Maze maze = mg.generate(30, 30);
        SearchableMaze searchableMaze = new SearchableMaze(maze);
        //solveProblem(searchableMaze, new BreadthFirstSearch());
        Solution sol=solveProblem(searchableMaze, new DepthFirstSearch());
        //solveProblem(searchableMaze, new BestFirstSearch());

        searchableMaze.beautifyPrintSol(sol);
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