package IO;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {

    private OutputStream stream;

    public MyCompressorOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    public void write(int b) {
        throw new NotImplementedException();
    }

    public void write(byte[] data) {
        //compress the byte array and write it into the stream.
    }
}
