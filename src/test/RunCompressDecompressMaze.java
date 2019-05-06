package test;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Aviadjo on 3/26/2017.
 */
public class RunCompressDecompressMaze {

    public static void main(String[] args) {

        String mazeFileName = "savedMaze.maze";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
        int[][] mazeData = new int[][] {
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 0, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 0, 0, 0, 0, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 0, 1, 1 ,1},
                {1, 0, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 1, 0, 0, 1, 1, 0, 1, 1, 1},
                {1, 1, 1, 0, 1, 0, 0, 1, 1, 1},
                {1, 1, 1, 0, 0, 0, 1, 1, 1, 1}
        };
        Maze maze = new Maze(mazeData, new Position(9, 3), new Position(0, 1));

        double sumcomp = 0;
        double maxcomp = Double.MIN_VALUE;
        double mincomp = Double.MAX_VALUE;
        int iterations = 100;
        for (int i = 0; i < iterations; i ++) {
            maze = mazeGenerator.generate(1000, 1000);
            //maze.print();
            try {
                // save maze to a file
                OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
                out.write(maze.toByteArray());
                double currcomp = ((MyCompressorOutputStream)out).getCompressionRate();
                sumcomp += currcomp;
                if (currcomp > maxcomp) maxcomp = currcomp;
                if (currcomp < mincomp) mincomp = currcomp;
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            byte[] savedMazeBytes = new byte[0];
            try {
                //read maze from file
                InputStream in = new MyDecompressorInputStream(new FileInputStream(mazeFileName));
                savedMazeBytes = new byte[maze.toByteArray().length];
                in.read(savedMazeBytes);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] res = maze.toByteArray();
            for (int j = 0; j < res.length; j++) {
                if (res[j] != savedMazeBytes[j]) {
                    System.out.println(j - Maze.HEADER_LENGTH + ", " + savedMazeBytes[j] + " -> " + res[j]);
                    break;
                }
            }

            Maze loadedMaze = new Maze(savedMazeBytes);
            boolean areMazesEquals = Arrays.equals(loadedMaze.toByteArray(), maze.toByteArray());
            if (!areMazesEquals) {
                System.out.println("Error: decompressed maze does not mach compressed maze. " + i);
                System.exit(0);
            }
        }
        System.out.println("Average Compression Rate: " + sumcomp / iterations);
        System.out.println("Best Compression Rate: " + mincomp);
        System.out.println("Worst Compression Rate: " + maxcomp);
    }

}
