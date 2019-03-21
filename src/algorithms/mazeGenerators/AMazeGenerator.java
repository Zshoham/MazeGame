package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator{

    protected Maze maze; //TODO: is needed?


    public long measureAlgorithmTimeMillis(int rows, int cols){
        long sTime=System.currentTimeMillis();
        maze=generate(rows, cols);
        long eTime=System.currentTimeMillis();
        return eTime-sTime;
    }
}
