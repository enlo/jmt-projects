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
package info.naiv.lab.java.jmt.text;

import info.naiv.lab.java.jmt.ClassicArrayUtils.ArrayIterator;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayCompareTo;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import info.naiv.lab.java.jmt.Lazy;
import info.naiv.lab.java.jmt.iteration.ReverseIterator;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.Serializable;
import java.text.BreakIterator;
import static java.text.BreakIterator.DONE;
import static java.text.BreakIterator.getCharacterInstance;
import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOfRange;
import java.util.Iterator;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Unicode Vector.
 *
 * @author enlo
 */
@EqualsAndHashCode(of = "source")
public final class UnicodeVector implements CharSequence, Comparable<UnicodeVector>, Cloneable, Iterable<UnicodeScalar>, Serializable {

    private static final long serialVersionUID = 4034478541129542140L;

    @Getter
    private final boolean decomposed;

    private final Lazy<UnicodeScalar[]> elements;

    @Getter
    private final String source;

    /**
     * コンストラクタ.
     *
     * @param source 基になる文字列.
     */
    public UnicodeVector(String source) {
        this(false, source);
    }

    /**
     * コンストラクタ.
     *
     * @param decomposed source が分解済みなら true.
     * @param source 基になる文字列.
     */
    public UnicodeVector(boolean decomposed, String source) {
        this.decomposed = decomposed;
        this.source = source;
        this.elements = new Analyzer();
    }

    public UnicodeVector(boolean decomposed, String source, Lazy<UnicodeScalar[]> elements) {
        this.decomposed = decomposed;
        this.source = source;
        this.elements = elements;
    }

    @Override
    public char charAt(int index) {
        return source.charAt(index);
    }

    @Override
    public UnicodeVector clone() {
        try {
            return (UnicodeVector) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    @Override
    public int compareTo(UnicodeVector o) {
        return arrayCompareTo(elements(), o.elements());
    }

    public UnicodeVector decompose() {
        if (decomposed) {
            return this;
        }
        else {
            return new UnicodeVector(true, normalize(source, NFD));
        }
    }

    /**
     *
     * @return ユニコード構成文字の配列.
     */
    @ReturnNonNull
    public UnicodeScalar[] elements() {
        return elements.get();
    }

    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public Iterator<UnicodeScalar> iterator() {
        return new ArrayIterator<>(elements());
    }

    @Override
    public int length() {
        return source.length();
    }

    @ReturnNonNull
    public Iterator<UnicodeScalar> reverseIterator() {
        return new ReverseIterator<>(asList(elements()));
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return source.subSequence(start, end);
    }

    /**
     * 部分文字列を取得.
     *
     * @param start 開始文字位置
     * @return 開始文字から終端まで
     */
    @ReturnNonNull
    public UnicodeVector subVector(int start) {
        return subVector(start, elements().length);
    }

    /**
     * 部分文字列を取得.
     *
     * @param start 開始文字位置
     * @param end 終端文字位置
     * @return
     */
    @ReturnNonNull
    public UnicodeVector subVector(int start, int end) {
        UnicodeScalar[] elm = elements();
        if (elm.length < start) {
            throw new ArrayIndexOutOfBoundsException(elm.length + "<" + start);
        }
        if (elm.length < end) {
            end = elm.length;
        }
        UnicodeScalar[] subseq = copyOfRange(elm, start, end);
        String substr = arrayToString(subseq);
        return new UnicodeVector(decomposed, substr, new Lazy<>(subseq));
    }

    @Override
    public String toString() {
        return source;
    }

    /**
     * 文字列を分析します.
     *
     * @return 分析結果.
     */
    private UnicodeScalar[] analyze() {
        List<UnicodeScalar> result = new ArrayList<>(source.length());
        BreakIterator it = getCharacterInstance();
        it.setText(source);
        for (int p = it.first(), i = it.next(); i != DONE; p = i, i = it.next()) {
            result.add(new UnicodeScalar(source.substring(p, i)));
        }
        UnicodeScalar[] array = new UnicodeScalar[result.size()];
        return result.toArray(array);
    }

    class Analyzer extends Lazy<UnicodeScalar[]> {

        @Override
        public UnicodeScalar[] initialValue() {
            return analyze();
        }
    }
}
