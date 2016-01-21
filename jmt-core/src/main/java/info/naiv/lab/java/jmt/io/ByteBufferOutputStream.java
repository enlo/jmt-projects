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
import java.io.OutputStream;
import static java.lang.Math.max;
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
public class ByteBufferOutputStream extends OutputStream {

    @Getter
    @Setter
    @NonNull
    private ByteBuffer byteBuffer;

    public ByteBufferOutputStream() {
        this(NIOUtils.DEFAULT_BUFFER_SIZE);
    }

    public ByteBufferOutputStream(int initialSize) {
        byteBuffer = allocate(initialSize);
    }

    public synchronized ByteBuffer copyBuffer() {
        return byteBuffer.duplicate();
    }

    public ByteBuffer flip() {
        return (ByteBuffer) byteBuffer.flip();
    }

    @Override
    public synchronized void write(int b) throws IOException {
        if (!byteBuffer.hasRemaining()) {
            expand(1);
        }
        byteBuffer.put((byte) b);
    }

    @Override
    public synchronized void write(byte[] b) throws IOException {
        if (byteBuffer.remaining() < b.length) {
            expand(b.length);
        }
        byteBuffer.put(b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) throws IOException {
        if (byteBuffer.remaining() < len) {
            expand(b.length);
        }
        byteBuffer.put(b, off, len);
    }

    private void expand(int length) {
        final int currentSize = byteBuffer.capacity();
        final int newSize = currentSize + max(currentSize / 2, length);
        ByteBuffer newBuffer = allocate(newSize);
        byteBuffer.flip();
        newBuffer.put(byteBuffer);
        byteBuffer = newBuffer;
    }

}
