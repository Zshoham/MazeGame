package IO;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MyDecompressorInputStream extends InputStream {

    private InputStream stream;

    //region Deprecated Members

    @Deprecated
    private boolean[] isRecreated;

    @Deprecated
    private int cols;

    @Deprecated
    private int rows;

    @Deprecated
    private int x, y;

    //endregion

    public MyDecompressorInputStream(InputStream stream) { this.stream = stream; }

    @Override
    public int read() {
        throw new NotImplementedException();
    }

    @Override
    public int read(byte[] dest) throws IOException {
        byte[] compressedMaze = new byte[stream.available()];
        int read = stream.read(compressedMaze);

        squashDecompress(dest, compressedMaze);

        return read;
    }

    private void squashDecompress(byte[] dest, byte[] compressedData) {
        System.arraycopy(compressedData, 0, dest, 0, Maze.HEADER_LENGTH);

        int resPos = Maze.HEADER_LENGTH;
        for (int i = Maze.HEADER_LENGTH; i < compressedData.length - 1; i++) {
            byte bitSet = compressedData[i];
            for (int j = 7; j >= 0 && resPos + j < dest.length; j--) {
                dest[resPos + j] = (byte) (bitSet & 0x01);
                bitSet = (byte) (bitSet >> 1);
            }
            resPos += 8;
        }

        byte remainder = (byte) ((dest.length - Maze.HEADER_LENGTH) % 8);

        if (remainder != 0) {
            byte bitSet = compressedData[compressedData.length - 1];
            for (int i = remainder - 1; i >= 0 ; i--) {
                dest[resPos + i] = (byte) (bitSet & 0x01);
                bitSet = (byte) (bitSet >> 1);
            }
        }
        else {
            byte bitSet = compressedData[compressedData.length - 1];
            for (int j = 7; j >= 0 && resPos + j < dest.length; j--) {
                dest[resPos + j] = (byte) (bitSet & 0x01);
                bitSet = (byte) (bitSet >> 1);
            }
        }
    }

    //region Deprecated Algorithms

    @Deprecated
    private void streamDecompress(byte[] dest, byte[] compressedData) {
        for (int i = 0; i < Maze.HEADER_LENGTH; i++) dest[i] = compressedData[i];

        int resPos = Maze.HEADER_LENGTH;
        byte toWrite = compressedData[Maze.HEADER_LENGTH];
        for (int i = Maze.HEADER_LENGTH + 1; i < compressedData.length; i++) {
            int amount = compressedData[i] & 0xff;
            for (int j = 0; j < amount; j++) dest[resPos++] = toWrite;
            toWrite = toWrite == 1 ? (byte)0 : (byte)1;
        }
    }

    @Deprecated
    private void doubleStreamDecompress(byte[] dest, byte[] compressedData) {
        for (int i = 0; i < Maze.HEADER_LENGTH; i++) dest[i] = compressedData[i];

        this.cols = Serializer.readShort(0, compressedData);
        this.rows = Serializer.readShort(2, compressedData);

        this.isRecreated = new boolean[rows * cols];

        byte[] compressedMazeData = Arrays.copyOfRange(compressedData, Maze.HEADER_LENGTH + 1, compressedData.length);

        this.x = 0; this.y = 0;
        int cPointer = 0;
        byte toWrite = compressedData[Maze.HEADER_LENGTH];
        ArrayList<Position> recreatedInStep;
        while (cPointer < compressedMazeData.length) {
            byte cByte = compressedMazeData[cPointer];
            cPointer++;
            int bX, bY;
            switch (cByte) {
                case 1:
                    byte horizontal = compressedMazeData[cPointer];
                    cPointer++;
                    recreateHorizontal(dest, horizontal, toWrite);
                    break;
                case -1:
                    short sHorizontal = Serializer.readShort(cPointer, compressedMazeData);
                    cPointer += 2;
                    recreateHorizontal(dest, sHorizontal, toWrite);
                    break;
                case 2:
                    byte vertical = compressedMazeData[cPointer];
                    cPointer++;
                    recreatedInStep = new ArrayList<>();
                    recreateVertical(dest, vertical, toWrite, x, y,recreatedInStep);
                    for (int i = 0; i < recreatedInStep.size(); i ++) {
                        Position iPos = recreatedInStep.get(i);
                        isRecreated[iPos.getColumnIndex() + iPos.getRowIndex()*cols] = true;
                    }
                    break;
                case -2:
                    short sVertical = Serializer.readShort(cPointer, compressedMazeData);
                    cPointer += 2;
                    recreatedInStep = new ArrayList<>();
                    recreateVertical(dest, sVertical, toWrite, x, y, recreatedInStep);
                    for (int i = 0; i < recreatedInStep.size(); i ++) {
                        Position iPos = recreatedInStep.get(i);
                        isRecreated[iPos.getColumnIndex() + iPos.getRowIndex()*cols] = true;
                    }
                    break;
                case Byte.MAX_VALUE:
                    byte tHorizontal = compressedMazeData[cPointer];
                    cPointer++;
                    byte tVertical = compressedMazeData[cPointer];
                    cPointer++;

                    bX = this.x; bY = this.y;
                    recreatedInStep = new ArrayList<>();
                    recreateHorizontal(dest, tHorizontal, toWrite);
                    recreateVertical(dest, tVertical, toWrite, bX, bY, recreatedInStep);
                    for (int i = 0; i < recreatedInStep.size(); i ++) {
                        Position iPos = recreatedInStep.get(i);
                        isRecreated[iPos.getColumnIndex() + iPos.getRowIndex()*cols] = true;
                    }

                    break;
                case Byte.MIN_VALUE:
                    short tbHorizontal = Serializer.readShort(cPointer, compressedMazeData);
                    cPointer += 2;
                    short tbVertical = Serializer.readShort(cPointer, compressedMazeData);
                    cPointer += 2;

                    bX = this.x; bY = this.y;
                    recreatedInStep = new ArrayList<>();
                    recreateHorizontal(dest, tbHorizontal,toWrite);
                    recreateVertical(dest, tbVertical,toWrite, bX, bY, recreatedInStep);
                    for (int i = 0; i < recreatedInStep.size(); i ++) {
                        Position iPos = recreatedInStep.get(i);
                        isRecreated[iPos.getColumnIndex() + iPos.getRowIndex()*cols] = true;
                    }
                    break;
                case -3:
                    short thHorizontal = Serializer.readShort(cPointer, compressedMazeData);
                    cPointer += 2;
                    byte thVertical = compressedMazeData[cPointer];
                    cPointer++;

                    bX = this.x; bY = this.y;
                    recreatedInStep = new ArrayList<>();
                    recreateHorizontal(dest, thHorizontal, toWrite);
                    recreateVertical(dest, thVertical, toWrite, bX, bY, recreatedInStep);
                    for (int i = 0; i < recreatedInStep.size(); i ++) {
                        Position iPos = recreatedInStep.get(i);
                        isRecreated[iPos.getColumnIndex() + iPos.getRowIndex()*cols] = true;
                    }
                    break;
                case -4:
                    byte tvHorizontal = compressedMazeData[cPointer];
                    cPointer++;
                    short tvVertical = Serializer.readShort(cPointer, compressedMazeData);
                    cPointer += 2;

                    bX = this.x; bY = this.y;
                    recreatedInStep = new ArrayList<>();
                    recreateHorizontal(dest, tvHorizontal, toWrite);
                    recreateVertical(dest, tvVertical, toWrite, bX, bY, recreatedInStep);
                    for (int i = 0; i < recreatedInStep.size(); i ++) {
                        Position iPos = recreatedInStep.get(i);
                        isRecreated[iPos.getColumnIndex() + iPos.getRowIndex()*cols] = true;
                    }
                    break;
            }

            while (x < cols && y < rows && isRecreated[x + y*cols]) {
                if (this.x == cols - 1) {
                    this.x = 0;
                    this.y++;
                }
                else x++;
            }
            toWrite = toWrite == 1 ? (byte)0 : (byte)1;
        }
        
    }

    @Deprecated
    private void recreateHorizontal(byte[] dest, int offset, byte toWrite) {
        for (int i = 0; i < offset; i++) {
            while (x < cols && y < rows && isRecreated[x + y*cols]) {
                if (this.x == cols - 1) {
                    this.x = 0;
                    this.y++;
                }
                else x++;
            }
            if (x >= cols || y >= rows ) return;

            if(dest[Maze.HEADER_LENGTH + (x + y*cols)] != -1 && dest[Maze.HEADER_LENGTH + (x + y*cols)] != toWrite)
                System.out.println("wrong wrong wrong");
            dest[Maze.HEADER_LENGTH + (x + y*cols)] = toWrite;
            isRecreated[x + y*cols] = true;
            if (this.x == cols - 1) {
                this.x = 0;
                this.y++;
            }
            else x++;
        }
    }

    @Deprecated
    private void recreateVertical(byte[] dest, int offset, byte toWrite, int tX, int tY, ArrayList<Position> recreated) {
        recreated.add(new Position(tY, tX));
        dest[Maze.HEADER_LENGTH + (tX + tY*cols)] = toWrite;
        tY++;
        offset--;

        for (int i = 0; i < offset && tY < rows; i++) {
            while (tY < rows && isRecreated[tX + tY * cols]) tY++;
            if(tY == rows) break;


            if(dest[Maze.HEADER_LENGTH + (tX + tY*cols)] != -1 && dest[Maze.HEADER_LENGTH + (tX + tY*cols)] != toWrite)
                System.out.println("wrong wrong wrong");
            dest[Maze.HEADER_LENGTH + (tX + tY*cols)] = toWrite;
            //isRecreated[tX + tY*cols] = true;
            recreated.add(new Position(tY, tX));
            tY++;
        }
    }

    //endregion
}
