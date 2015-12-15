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
package info.naiv.lab.java.jmt;

import static info.naiv.lab.java.jmt.Arguments.*;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import info.naiv.lab.java.jmt.mark.ThreadSafety;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import lombok.Value;

/**
 *
 * @author enlo
 */
@ThreadSafety
@Value
public class SizedBytes implements Serializable, Cloneable {

    private static final long serialVersionUID = -5793468232263643179L;

    final byte[] data;
    final int size;

    public SizedBytes(byte[] data) {
        nonNull(data, "data");
        this.size = data.length;
        this.data = data;
    }

    public SizedBytes(int size, byte[] data) {
        nonMinus(size, "size");
        if (0 < size) {
            nonNull(data, "data");
            between(size, 0, data.length, "size");
            this.size = size;
            this.data = data;
        }
        else {
            this.size = 0;
            this.data = data;
        }
    }

    public SizedBytes(ByteArrayOutputStream os) {
        nonNull(os, "os");
        this.size = os.size();
        this.data = os.toByteArray();
    }

    public ByteArrayInputStream newInputStream() {
        return new ByteArrayInputStream(data, 0, size);
    }

    /**
     *
     * @return 指定されたサイズにあったバイト列を作成して戻す.
     */
    public byte[] toByteArray() {
        return Arrays.copyOf(data, size);
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    @ReturnNonNull
    protected SizedBytes clone() {
        try {
            return (SizedBytes) super.clone(); //To change body of generated methods, choose Tools | Templates.
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

}
