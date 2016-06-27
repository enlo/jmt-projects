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
import javax.annotation.Nonnull;

/**
 * CharBuffer を利用した Joiner. 短い文字列用.
 *
 * @author enlo
 */
public class CharBufferJoiner extends AbstractJoiner<CharSequence, CharBuffer> {

    /**
     * カンマ「,」区切り.
     */
    public static final CharBufferJoiner COMMADELIMITED = new CharBufferJoiner(",");

    /**
     * ドット「.」区切り.
     */
    public static final CharBufferJoiner DOTTED = new CharBufferJoiner(".");
    /**
     * ハイフン「-」区切り.
     */
    public static final CharBufferJoiner HYPHENATED = new CharBufferJoiner("-");
    /**
     * 区切り文字無し.
     */
    public static final CharBufferJoiner SIMPLE = new CharBufferJoiner();
    /**
     * スラッシュ「/」区切り.
     */
    public static final CharBufferJoiner SLASHED = new CharBufferJoiner("/");
    /**
     * 既定のバッファサイズ. 1024byte
     */
    protected static final int DEFAULT_CAPACITY = 1024;

    /**
     * インスタンスの取得.
     *
     * @param delim 区切り文字.
     * @return インスタンス.
     */
    @Nonnull
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

    private static CharBuffer addCharSequence(CharBuffer obj, CharSequence value) {
        return obj.append(value);
    }

    /**
     * キャパシティ.
     */
    protected final int capacity;

    /**
     * 既定のバッファサイズを使用したコンストラクター. 区切り文字無し.
     *
     * @see DEFAULT_CAPACITY
     *
     */
    public CharBufferJoiner() {
        super(new SimpleAdder());
        this.capacity = DEFAULT_CAPACITY;
    }

    /**
     * バッファサイズを指定したコンストラクター. 区切り文字無し.
     *
     * @param capacity バッファサイズ.
     */
    public CharBufferJoiner(int capacity) {
        super(new SimpleAdder());
        this.capacity = capacity;
    }

    /**
     * 区切り文字を指定したコンストラクター.
     *
     * @param delim 区切り文字.
     */
    public CharBufferJoiner(CharSequence delim) {
        this(DEFAULT_CAPACITY, delim);
    }

    /**
     * キャパシティと区切り文字を指定したコンストラクター.
     *
     * @param capacity キャパシティ.
     * @param delim 区切り文字.
     */
    public CharBufferJoiner(int capacity, final CharSequence delim) {
        super(new SimpleAdder(), new Adder<CharSequence, CharBuffer>() {
            @Override
            public CharBuffer add(CharBuffer obj, CharSequence value, int idx) {
                obj.append(delim);
                return addCharSequence(obj, value);
            }
        });
        this.capacity = capacity;
    }

    /**
     * adder を指定したコンストラクター.
     *
     * @param first 最初の adder
     * @param more 2回目以降の adder
     */
    public CharBufferJoiner(Adder<CharSequence, CharBuffer> first, Adder<CharSequence, CharBuffer> more) {
        this(DEFAULT_CAPACITY, first, more);
    }

    /**
     * キャパシティと adder を指定したコンストラクター.
     *
     * @param capacity キャパシティ
     * @param first 最初の adder
     * @param more 2回目以降の adder
     */
    public CharBufferJoiner(int capacity, Adder<CharSequence, CharBuffer> first, Adder<CharSequence, CharBuffer> more) {
        super(first, more);
        this.capacity = capacity;
    }

    /**
     * キャパシティが変更された CharBufferJoiner を取得.
     *
     * @param newCapacity 新しいキャパシティ.
     * @return キャパシティを変更した CharBufferJoiner.
     */
    public CharBufferJoiner newCapacity(int newCapacity) {
        return new CharBufferJoiner(newCapacity, first, more);
    }

    @Override
    protected CharBuffer createResult() {
        return CharBuffer.allocate(capacity);
    }

    @Override
    protected CharBuffer postLoop(CharBuffer r, int count) {
        return (CharBuffer) r.flip();
    }

    private static class SimpleAdder implements Adder<CharSequence, CharBuffer> {

        @Override
        public CharBuffer add(CharBuffer obj, CharSequence value, int idx) {
            return addCharSequence(obj, value);
        }
    }

}
