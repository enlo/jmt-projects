/*
 * The MIT License
 *
 * Copyright 2017 enlo.
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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayCompareTo;
import info.naiv.lab.java.jmt.Lazy;
import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.iteration.MappingIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author enlo
 */
@ThreadSafe
@EqualsAndHashCode(of = "source")
@RequiredArgsConstructor
public class UnicodeText implements Comparable<UnicodeText>, Iterable<String>, Serializable {

    /**
     * 大小文字を無視する比較
     */
    public static final Comparator<UnicodeText> CASE_INSENSITIVE_ORDER = new Comparator<UnicodeText>() {
        @Override
        public int compare(UnicodeText o1, UnicodeText o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    };

    public static final UnicodeText EMPTY = new UnicodeText("");

    private static final int BUFFER = 64;

    private static final long serialVersionUID = 8023601816112880571L;

    /**
     * 小文字に変換して比較
     *
     * @param lhs 比較する文字列1
     * @param rhs 比較する文字列2
     * @return 一致すれば true.
     */
    public static boolean equalsIgnoreCase(UnicodeText lhs, UnicodeText rhs) {
        if (lhs == rhs) {
            return true;
        }
        else {
            if (lhs == null || rhs == null) {
                return false;
            }
            else {
                return CASE_INSENSITIVE_ORDER.compare(lhs, rhs) == 0;
            }
        }
    }

    /**
     * 文字列が null か 空なら true.
     *
     * @param str 対象文字列
     * @return 文字列が null か 空なら true.
     */
    public static boolean isEmpty(UnicodeText str) {
        return str == null || str.source.length() == 0;
    }

    public static UnicodeText valueOf(String value) {
        if (Misc.isEmpty(value)) {
            return EMPTY;
        }
        else {
            return new UnicodeText(value);
        }
    }

    transient volatile Lazy<UnicodeVector> decomposed = new Decomp();

    final UnicodeVector source;

    public UnicodeText(String value) {
        this.source = new UnicodeVector(value);
    }

    @Override
    public int compareTo(UnicodeText o) {
        return decomposed.get().compareTo(o.decomposed.get());
    }

    /**
     * 文字列が存在するかどうか.
     *
     * @see UniCord#indexOf(Rope)
     *
     * @param search 検索対象
     * @return 検索対象が存在すればtrue. ただし、検索対象が空なら常にfalse.
     */
    public boolean contains(UnicodeText search) {
        if (isEmpty(search)) {
            return false;
        }
        return indexOf(search) >= 0;
    }

    /**
     * 文字列が存在するかどうか.
     *
     * @see UniCord#indexOf(Rope)
     *
     * @param search 検索対象
     * @return 検索対象が存在すればtrue. ただし、検索対象が空なら常にfalse.
     */
    public boolean contains(String search) {
        if (Misc.isEmpty(search)) {
            return false;
        }
        return contains(valueOf(search));
    }

    /**
     * 指定された文字列で終了しているかどうか判断する.
     *
     * @param str 検査対象の文字列.
     * @param offset オフセット
     * @return 指定された文字列で終了していれば true.
     */
    public boolean endsWith(String str, int offset) {
        return endsWith(valueOf(str), offset);
    }

    /**
     * 指定された文字列で終了しているかどうか判断する.
     *
     * @param str 検査対象の文字列.
     * @param offset オフセット
     * @return 指定された文字列で終了していれば true.
     */
    public boolean endsWith(UnicodeText str, int offset) {
        return endsWith(str.decomposed.get().elements(), offset);
    }

    /**
     * 文字列の検索
     *
     * @param searchString 検索する文字列
     * @return 見つかった位置。見つからない場合は-1.
     */
    public int indexOf(@Nonnull UnicodeText searchString) {
        return indexOf(searchString, 0);
    }

    /**
     * 文字列の検索
     *
     * @param searchString 検索する文字列
     * @param offset 検索開始位置
     * @return 見つかった位置。見つからない場合は-1.
     */
    public int indexOf(@Nonnull UnicodeText searchString, int offset) {
        UnicodeScalar[] search = searchString.decomposed.get().elements();
        UnicodeScalar[] elem = decomposed.get().elements();
        for (int i = offset; i < elem.length; i++) {
            if (startsWith(search, i)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iter(source.iterator());
    }

    /**
     * すべて小文字に変換
     *
     * @return すべて小文字に変換した文字列.
     */
    @Nonnull
    public UnicodeText toLowerCase() {
        String lc = source.getSource().toLowerCase(Locale.getDefault());
        return valueOf(lc);
    }

    /**
     * すべて小文字に変換
     *
     * @param locale ロケール
     * @return すべて小文字に変換した文字列
     */
    @Nonnull
    public UnicodeText toLowerCase(Locale locale) {
        String lc = source.getSource().toLowerCase(locale);
        return valueOf(lc);
    }

    @Override
    public String toString() {
        return source.getSource();
    }

    private boolean endsWith(UnicodeScalar[] search, int offset) {
        int len = search.length;
        UnicodeScalar[] elem = decomposed.get().elements();
        int idx = elem.length - offset - len;
        if (idx < 0) {
            return false;
        }
        return arrayCompareTo(elem, idx, len, search, 0, len) == 0;
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        decomposed = new Lazy<>((UnicodeVector) stream.readObject());
    }

    private boolean startsWith(UnicodeScalar[] search, int offset) {
        int len = search.length;
        UnicodeScalar[] elem = decomposed.get().elements();
        int idx = elem.length - offset - len;
        if (idx < 0) {
            return false;
        }
        return arrayCompareTo(elem, offset, len, search, 0, len) == 0;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeObject(decomposed.get());
    }

    static class Iter extends MappingIterator<UnicodeScalar, String> {

        Iter(Iterator<UnicodeScalar> arg0) {
            super(arg0, new Function1<UnicodeScalar, String>() {
              @Override
              public String apply(UnicodeScalar a1) {
                  return a1.getElement();
              }
          });
        }
    }

    class Decomp extends Lazy<UnicodeVector> {

        @Override
        protected UnicodeVector initialValue() {
            return UnicodeVectorCache.getDecomposed(source);
        }

    }

    public String toDecomposedString() {
        return decomposed.get().getSource();
    }

    
}
