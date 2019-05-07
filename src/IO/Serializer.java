package IO;

import java.util.ArrayList;

/**
 * Provides Serialization functionality for writing and reading data into byte array.
 */
public final class Serializer {

    private Serializer() {}

    /**
     * Writes a short into the byte array.
     * @param data the short to be written.
     * @param pointer pointer to the position in the array from which to start writing.
     * @param dest the byte array to which to write.
     * @return pointer to the next byte after writing.
     */
    public static int write(short data, int pointer, byte[] dest) {
        dest[pointer++] = (byte) (data >> 8);
        dest[pointer++] = (byte) (data & 0xff);
        return pointer;
    }

    /**
     * Writes an int into the byte array.
     * @param data the int to be written.
     * @param pointer pointer to the position in the array from which to start writing.
     * @param dest the byte array to which to write.
     * @return pointer to the next byte after writing.
     */
    public static int write(int data, int pointer, byte[] dest) {
        dest[pointer++] = (byte)(data >> 24);
        dest[pointer++] = (byte)(data >> 16);
        dest[pointer++] = (byte)(data >> 8);
        dest[pointer++] = (byte)(data & 0xff);
        return pointer;
    }

    /**
     * Write an int into an ArrayList of Bytes.
     * @param data the int to be written.
     * @param dest the ArrayList to which to write.
     */
    public static void write(int data, ArrayList<Byte> dest) {
        dest.add((byte) (data >> 8));
        dest.add((byte) (data & 0xff));
    }

    /**
     * Reads a short from a byte array.
     * @param pointer pointer to the position in the array from which ti start reading.
     * @param src the array from which to read.
     * @return the short read from the array.
     */
    public static short readShort(int pointer, byte[] src) {
        return (short)(((src[pointer++] & 0xff) << 8) | (src[pointer++] & 0xff));
    }

    /**
     * Reads an int from a byte array.
     * @param pointer pointer to the position in the array from which ti start reading.
     * @param src the array from which to read.
     * @return the int read from the array.
     */
    public static int readInt(int pointer, byte[] src) {
        return (src[pointer++] << 24) | ((src[pointer++] & 0xff) << 16)
                | ((src[pointer++] & 0xff) << 8) | (src[pointer++] & 0xff);
    }

}
