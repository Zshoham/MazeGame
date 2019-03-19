package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator{
    protected Maze maze; //TODO: is needed?


    public abstract Maze generate(int rows, int cols);

    public long measureAlgorithmTimeMillis(int rows, int cols){
        long sTime=System.currentTimeMillis();
        maze=generate(rows, cols);
        long eTime=System.currentTimeMillis();
        return eTime-sTime;
    }
}