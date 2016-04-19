/*
 * The MIT License
 *
 * Copyright 2015 enlo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package info.naiv.lab.java.jmt.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import static java.nio.ByteBuffer.allocate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author enlo
 */
@AllArgsConstructor
public class ByteBufferInputStream extends InputStream {

    @Getter
    @Setter
    @NonNull
    private ByteBuffer byteBuffer;

    /**
     *
     */
    public ByteBufferInputStream() {
        this(NIOUtils.DEFAULT_BUFFER_SIZE);
    }

    /**
     *
     * @param bufferSize
     */
    public ByteBufferInputStream(int bufferSize) {
        this(allocate(bufferSize));
        byteBuffer.flip();
    }

    @Override
    public int available() throws IOException {
        return byteBuffer.remaining();
    }

    @Override
    public int read() throws IOException {
        if (!byteBuffer.hasRemaining()) {
            return -1;
        }
        return byteBuffer.get();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = Math.min(byteBuffer.remaining(), len);
        if (count == 0) {
            return -1;
        }
        byteBuffer.get(b, off, len);
        return count;
    }

}
