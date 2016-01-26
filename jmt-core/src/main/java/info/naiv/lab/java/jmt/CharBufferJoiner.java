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
package info.naiv.lab.java.jmt;

import java.nio.CharBuffer;

/**
 * CharBuffer を利用した Joiner. 短い文字列用.
 *
 * @author enlo
 */
public class CharBufferJoiner extends AbstractJoiner<Object, CharBuffer> {

    public static final CharBufferJoiner COMMADELIMITED = new CharBufferJoiner(",");
    public static final CharBufferJoiner DOTTED = new CharBufferJoiner(".");
    public static final CharBufferJoiner HYPHENATED = new CharBufferJoiner("-");
    public static final CharBufferJoiner SIMPLE = new CharBufferJoiner();
    public static final CharBufferJoiner SLASHED = new CharBufferJoiner("/");
    protected static final int DEFAULT_CAPACITY = 1024;

    public static CharBufferJoiner valueOf(String delim) {
        if (delim == null) {
            return SIMPLE;
        }
        switch (delim) {
            case ",":
                return COMMADELIMITED;
            case ".":
                return DOTTED;
            case "-":
                return HYPHENATED;
            case "/":
                return SLASHED;
            default:
                return new CharBufferJoiner(delim);
        }
    }

    private static CharBuffer addObject(CharBuffer obj, Object value) {
        if (value instanceof CharSequence) {
            return obj.append((CharSequence) value);
        }
        else if (value instanceof char[]) {
            return obj.put((char[]) value);
        }
        else if (value instanceof Character) {
            return obj.put((Character) value);
        }
        else {
            return obj.put(String.valueOf(value));
        }
    }

    protected final int capacity;

    public CharBufferJoiner() {
        super(new SimpleAdder());
        this.capacity = DEFAULT_CAPACITY;
    }

    public CharBufferJoiner(int capacity) {
        super(new SimpleAdder());
        this.capacity = capacity;
    }

    public CharBufferJoiner(CharSequence delim) {
        this(DEFAULT_CAPACITY, delim);
    }

    public CharBufferJoiner(int capacity, final CharSequence delim) {
        super(new SimpleAdder(), new Adder<Object, CharBuffer>() {
            @Override
            public CharBuffer add(CharBuffer obj, Object value, int idx) {
                obj.append(delim);
                return addObject(obj, value);
            }
        });
        this.capacity = capacity;
    }

    public CharBufferJoiner(Adder<Object, CharBuffer> first, Adder<Object, CharBuffer> more) {
        this(DEFAULT_CAPACITY, first, more);
    }

    public CharBufferJoiner(int capacity, Adder<Object, CharBuffer> first, Adder<Object, CharBuffer> more) {
        super(first, more);
        this.capacity = capacity;
    }

    public CharBufferJoiner newCapacity(int newCapacity) {
        return new CharBufferJoiner(newCapacity, first, more);
    }

    @Override
    protected CharBuffer createResult() {
        return CharBuffer.allocate(capacity);
    }

    private static class SimpleAdder implements Adder<Object, CharBuffer> {

        @Override
        public CharBuffer add(CharBuffer obj, Object value, int idx) {
            return addObject(obj, value);
        }
    }

}
