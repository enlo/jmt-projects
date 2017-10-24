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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayAsIterable;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayCompareTo;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import info.naiv.lab.java.jmt.Lazy;
import info.naiv.lab.java.jmt.iteration.ReverseIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Unicode Vector.
 *
 * @author enlo
 */
@ThreadSafe
@EqualsAndHashCode(of = "source")
public final class UnicodeVector implements Comparable<UnicodeVector>, Cloneable, Iterable<UnicodeScalar>, Serializable {

    private static final long serialVersionUID = 4034478541129542140L;

    @Getter
    private final boolean decomposed;

    private volatile transient Lazy<UnicodeScalar[]> elements;

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

    public CharSequence asCharSequence() {
        return source;
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
    @Nonnull
    public UnicodeScalar[] elements() {
        return elements.get();
    }

    public UnicodeScalar get(int index) {
        return elements()[index];
    }

    public String getString(int index) {
        return elements()[index].getElement();
    }

    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public Iterator<UnicodeScalar> iterator() {
        return arrayAsIterable(elements()).iterator();
    }

    public int length() {
        return elements().length;
    }

    @Nonnull
    public Iterator<UnicodeScalar> reverseIterator() {
        return new ReverseIterator<>(asList(elements()));
    }

    /**
     * 部分文字列を取得.
     *
     * @param start 開始文字位置
     * @return 開始文字から終端まで
     */
    @Nonnull
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
    @Nonnull
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
    @Nonnull
    private UnicodeScalar[] analyze() {
        List<UnicodeScalar> result = new ArrayList<>(source.length());
        BreakIterator it = getCharacterInstance();
        it.setText(source);
        for (int p = it.first(), i = it.next(); i != DONE; p = i, i = it.next()) {
            result.add(UnicodeScalar.valueOf(source, p, i));
        }
        UnicodeScalar[] array = new UnicodeScalar[result.size()];
        return result.toArray(array);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        elements = new Lazy<>((UnicodeScalar[]) stream.readObject());
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(elements.get());
    }

    class Analyzer extends Lazy<UnicodeScalar[]> {

        Analyzer() {
        }

        Analyzer(UnicodeScalar[] init) {
            super(init);
        }

        @Override
        public UnicodeScalar[] initialValue() {
            return analyze();
        }

        @Override
        protected UnicodeScalar[] rawValue() {
            return super.rawValue();
        }

    }

}
