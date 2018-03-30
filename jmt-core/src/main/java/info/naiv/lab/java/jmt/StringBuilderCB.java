/*
 * The MIT License
 *
 * Copyright 2018 enlo.
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

import java.io.Serializable;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public final class StringBuilderCB implements Appendable, CharSequence, Serializable {

    private static final int ST_EMPTY = 0;
    private static final int ST_UNIQUE = 1;
    private static final long serialVersionUID = -6430211030297471150L;

    private int length;
    private String nullValue = "null";
    private int nullValueLength = 4;
    private int state = ST_EMPTY;
    private List<String> strings;

    public StringBuilderCB() {
        strings = new LinkedList<>();
    }

    public StringBuilderCB(String initialValue) {
        this();
        append(initialValue);
    }

    public StringBuilderCB append(String value) {
        if (value != null) {
            return appendNonNull(value);
        }
        else {
            return appendNull();
        }
    }

    @Override
    public StringBuilderCB append(CharSequence csq) {
        return append(String.valueOf(csq));
    }

    @Override
    public StringBuilderCB append(CharSequence csq, int start, int end) {
        if (csq != null) {
            return appendNonNull(csq.subSequence(start, end).toString());
        }
        else {
            return appendNull();
        }
    }

    @Override
    public StringBuilderCB append(char c) {
        strings.add(Character.toString(c));
        length += 1;
        return this;
    }

    public StringBuilderCB append(int value) {
        return append(Integer.toString(value));
    }

    public StringBuilderCB append(long value) {
        return append(Long.toString(value));
    }

    public StringBuilderCB append(short value) {
        return append(Short.toString(value));
    }

    public StringBuilderCB append(double value) {
        return append(Double.toString(value));
    }

    public StringBuilderCB append(float value) {
        return append(Float.toString(value));
    }

    public StringBuilderCB append(byte value) {
        return append(Byte.toString(value));
    }

    public StringBuilderCB append(boolean value) {
        return append(Boolean.toString(value));
    }

    public StringBuilderCB append(char[] value) {
        if (value != null) {
            return appendNonNull(new String(value));
        }
        else {
            return appendNull();
        }
    }

    public StringBuilderCB append(byte[] value, Charset charset) {
        if (value != null) {
            if (charset == null) {
                charset = Charset.defaultCharset();
            }
            return appendNonNull(new String(value, charset));
        }
        else {
            return appendNull();
        }
    }

    public StringBuilderCB append(byte[] value, String charset) {
        if (value != null) {
            Charset cs;
            if (charset != null) {
                cs = Charset.forName(charset);
            }
            else {
                cs = Charset.defaultCharset();
            }
            return appendNonNull(new String(value, cs));
        }
        else {
            return appendNull();
        }
    }

    public StringBuilderCB append(Object value) {
        return append(String.valueOf(value));
    }

    public StringBuilderCB appendFormat(String format, Object arg1) {
        return append(String.format(format, arg1));
    }

    public StringBuilderCB appendFormat(String format, Object arg1, Object arg2) {
        return append(String.format(format, arg1, arg2));
    }

    public StringBuilderCB appendFormat(String format, Object... args) {
        return append(String.format(format, args));
    }

    @Override
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    public int length() {
        return length;
    }

    @Nonnull
    public StringBuilderCB nullValue(String nullValue) {
        this.nullValue = String.valueOf(nullValue);
        this.nullValueLength = this.nullValue.length();
        return this;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    public String substring(int start) {
        return toString().substring(start);
    }

    public String substring(int start, int end) {
        return toString().substring(start, end);
    }

    @Override
    public String toString() {
        switch (state) {
            case ST_EMPTY:
                return "";
            case ST_UNIQUE:
                return strings.get(0);
            default:
                break;
        }
        CharBuffer cb = CharBuffer.allocate(length);
        for (String s : strings) {
            cb.append(s);
        }
        cb.flip();
        String result = cb.toString();
        List<String> another = new LinkedList<>();
        another.add(result);
        strings = another;
        state = ST_UNIQUE;
        return result;
    }

    private StringBuilderCB appendNonNull(@Nonnull String value) {
        strings.add(value);
        length += value.length();
        state++;
        return this;
    }

    @Nonnull
    private StringBuilderCB appendNull() {
        if (0 < nullValueLength) {
            strings.add(nullValue);
            length += nullValueLength;
            state++;
        }
        return this;
    }

}
