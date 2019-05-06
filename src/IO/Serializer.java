package IO;

import java.util.ArrayList;

public final class Serializer {

    private Serializer() {}

    public static int write(short data, int pointer, byte[] dest) {
        dest[pointer++] = (byte) (data >> 8);
        dest[pointer++] = (byte) (data & 0xff);
        return pointer;
    }

    public static int write(int data, int pointer, byte[] dest) {
        dest[pointer++] = (byte)(data >> 24);
        dest[pointer++] = (byte)(data >> 16);
        dest[pointer++] = (byte)(data >> 8);
        dest[pointer++] = (byte)(data & 0xff);
        return pointer;
    }

    public static void write(int data, ArrayList<Byte> dest) {
        dest.add((byte) (data >> 8));
        dest.add((byte) (data & 0xff));
    }

    public static short readShort(int pointer, byte[] src) {
        return (short)(((src[pointer++] & 0xff) << 8) | (src[pointer++] & 0xff));
    }

    public static int readInt(int pointer, byte[] src) {
        return (src[pointer++] << 24) | ((src[pointer++] & 0xff) << 16)
                | ((src[pointer++] & 0xff) << 8) | (src[pointer++] & 0xff);
    }

}
