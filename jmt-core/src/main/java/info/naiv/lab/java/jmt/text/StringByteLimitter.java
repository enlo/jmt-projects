/*
 * The MIT License
 *
 * Copyright 2016 enlo.
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
package info.naiv.lab.java.jmt.text;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

/**
 *
 * @author enlo
 */
public class StringByteLimitter {

    /**
     *
     * @param source
     * @param encoding
     * @param limitBytes
     * @return
     */
    public static final String truncate(String source, Charset encoding, int limitBytes) {
        int len = source.length();
        CharsetEncoder encoder = encoding.newEncoder();
        if (!isLimitOver(encoder, len, limitBytes)) {
            return source;
        }

        int capacity = Math.min(len, limitBytes);
        CharBuffer buff = CharBuffer.wrap(new char[capacity]);
        int procSize = Math.min(len, buff.length());
        source.getChars(0, procSize, buff.array(), 0);
        return truncate(buff, encoder, limitBytes).toString();
    }

    /**
     *
     * @param source
     * @param encoder
     * @param limitBytes
     * @return
     */
    public static CharBuffer truncate(CharBuffer source, CharsetEncoder encoder, int limitBytes) {
        if (!isLimitOver(encoder, source.limit(), limitBytes)) {
            return source;
        }
        final ByteBuffer out = ByteBuffer.allocate(limitBytes);
        encoder.reset();
        CoderResult cr = source.hasRemaining()
                ? encoder.encode(source, out, true)
                : CoderResult.UNDERFLOW;
        if (cr.isUnderflow()) {
            encoder.flush(out);
        }
        return (CharBuffer) source.flip();
    }

    private static boolean isLimitOver(CharsetEncoder encoder, int length, int limitBytes) {
        return (limitBytes < encoder.maxBytesPerChar() * length);
    }

    private StringByteLimitter() {
    }
}
