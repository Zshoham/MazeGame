package IO;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {

    private InputStream stream;

    public MyDecompressorInputStream(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public int read() {
        throw new NotImplementedException();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    @Override
    public int available() throws IOException {
        return super.available();
    }
}
