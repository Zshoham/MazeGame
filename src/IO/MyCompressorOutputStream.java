package IO;

import algorithms.mazeGenerators.Maze;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream stream;

    private float compressionRate;

    //region Deprecated Members

    @Deprecated
    boolean[] isExplored;

    @Deprecated
    int cols;

    @Deprecated
    int rows;

    //endregion

    public MyCompressorOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void write(int b) {
        throw new NotImplementedException();
    }

    public void write(byte[] data) throws IOException {
        ArrayList<Byte> compressedData = squashCompress(data);

        byte[] res = new byte[compressedData.size()];
        for (int i = 0; i < res.length; i++) res[i] = compressedData.get(i);
        this.compressionRate = (float)res.length / (float)data.length;

        this.stream.write(res);
    }

    private ArrayList<Byte> squashCompress(byte[] data) {
        ArrayList<Byte> compressedData = new ArrayList<>();
        for (int i = 0; i < Maze.HEADER_LENGTH; i++) compressedData.add(data[i]);

        for (int i = Maze.HEADER_LENGTH; i < data.length; i += 8) {
            byte bitSet = 0;
            for (int j = 0; j < 8 && (i + j) < data.length; j++) {
                bitSet = (byte) (bitSet << 1);
                bitSet = (byte) (bitSet | data[i + j]);
            }
            compressedData.add(bitSet);
        }

        return compressedData;
    }

    //region Deprecated Algorithms

    @Deprecated
    private ArrayList<Byte> streamCompress(byte[] data) {
        ArrayList<Byte> compressedData = new ArrayList<>();
        for (int i = 0; i < Maze.HEADER_LENGTH; i++) compressedData.add(data[i]);

        int i = Maze.HEADER_LENGTH;
        compressedData.add(data[i]);
        while (i < data.length) {
            byte current = data[i];
            byte count = 0;
            while (i < data.length && data[i] == current) {
                if(count == (byte)0xff){
                    compressedData.add(count);
                    compressedData.add((byte)0);
                    count = 0;
                }
                count++;
                i++;
            }
           compressedData.add(count);
        }

        return compressedData;
    }

    @Deprecated
    private ArrayList<Byte> doubleStreamCompress(byte[] data) {
        ArrayList<Byte> compressedData = new ArrayList<>();
        for (int i = 0; i < Maze.HEADER_LENGTH; i++) compressedData.add(data[i]);
        compressedData.add(data[Maze.HEADER_LENGTH]);

        this.cols = Serializer.readShort(0, data);
        this.rows = Serializer.readShort(2, data);

        this.isExplored = new boolean[rows * cols];

        byte[] mazeData = Arrays.copyOfRange(data, Maze.HEADER_LENGTH, data.length);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (isExplored[x + y*cols]) continue;

                int horizontal = scanHorizontal(mazeData, x, y);
                int vertical = scanVertical(mazeData, x, y);

                encode(compressedData, horizontal, vertical);
            }
        }

        return compressedData;
    }

    @Deprecated
    private void encode(ArrayList<Byte> compressedData, int horizontal, int vertical) {
        if (vertical == -1) {
            if (horizontal <= 0xff) {
                compressedData.add((byte)1);
                compressedData.add((byte)horizontal);
            }
            else {
                compressedData.add((byte)-1);
                short sHorizontal = (short)horizontal;
                Serializer.write(sHorizontal, compressedData);
            }
        }
        else if (horizontal == 1) {
            if(vertical <= 0xff) {
                compressedData.add((byte)2);
                compressedData.add((byte)vertical);
            }
            else {
                compressedData.add((byte)-2);
                short sVertical = (short)vertical;
                Serializer.write(sVertical, compressedData);
            }
        }
        else {
            if (horizontal <= 0xff && vertical <= 0xff) {
                compressedData.add(Byte.MAX_VALUE);
                compressedData.add((byte)horizontal);
                compressedData.add((byte)vertical);
            }
            else if (horizontal > 0xff && vertical > 0xff) {
                compressedData.add(Byte.MIN_VALUE);
                short sHorizontal = (short)horizontal;
                short sVertical = (short)vertical;

                Serializer.write(sHorizontal, compressedData);
                Serializer.write(sVertical, compressedData);
            }
            else if (vertical <= 0xff) {
                compressedData.add((byte)-3);
                short sHorizontal = (short)horizontal;

                Serializer.write(sHorizontal, compressedData);
                compressedData.add((byte)vertical);
            }
            //TODO: change the condition (this condition is always true.)
            else if (horizontal <= 0xff) {
                compressedData.add((byte)-4);
                short sVertical = (short)vertical;

                compressedData.add((byte)horizontal);
                Serializer.write(sVertical, compressedData);
            }
        }
    }

    @Deprecated
    private int scanVertical(byte[] data, int x, int y) {
        int count = 1;
        int offset = 1;
        isExplored[x + y*cols] = true;
        while (y + offset < rows && (isExplored[x+ (y+offset)*cols] || data[x + y*cols] == data[x + (y+offset)*cols])) {
            if (isExplored[x + (y+offset)*cols]) {
                offset++;
                continue;
            }
            count++;
            offset++;
        }
        offset = 1;
        if (count >= 3) {
            for (int i = 0; i < count - 1 && offset < rows; i++) {
                while (y+offset < rows && isExplored[x + (y+offset)*cols]) offset++;
                if(y+offset == rows) break;

                isExplored[x + (y+offset)*cols] = true;
            }
        }
        else count = -1;

        return count;
    }

    @Deprecated
    private int scanHorizontal(byte[] data, int x, int y) {
        int count = 1;
        int offset = 1;
        isExplored[x + y*cols] = true;
        while (count <= 0xffff && ((x + y*cols) + offset) < data.length && (isExplored[(x + y*cols) + offset] || data[x + y*cols] == data[(x + y*cols) + offset])) {
            if (isExplored[(x + y*cols) + offset]) {
                offset++;
                continue;
            }
            isExplored[(x + y*cols) + offset] = true;
            count++;
            offset++;
        }
        return count;
    }

    //endregion

    public float getCompressionRate() { return this.compressionRate; }


}
