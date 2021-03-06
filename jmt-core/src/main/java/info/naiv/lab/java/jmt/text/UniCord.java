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

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayCompareTo;
import info.naiv.lab.java.jmt.iteration.MappingIterator;
import info.naiv.lab.java.jmt.Misc;
import info.naiv.lab.java.jmt.fx.Function1;
import java.io.Serializable;
import java.text.BreakIterator;
import static java.text.BreakIterator.getCharacterInstance;
import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * UniCord. 文字列を正規化したうえで、サロゲートペアまで含めて比較を行う. String より処理が重いので Cord.
 *
 * @author enlo
 *
 */
@ThreadSafe
@EqualsAndHashCode
@RequiredArgsConstructor
public final class UniCord implements Comparable<UniCord>, Iterable<String>, Serializable {

    /**
     * 大小文字を無視する比較
     */
    public static final Comparator<? super UniCord> CASE_INSENSITIVE_ORDER = new Comparator<UniCord>() {
        @Override
        public int compare(UniCord o1, UniCord o2) {
            return o1.toLowerCase().compareNormalized(o2.toLowerCase());
        }
    };

    /**
     * 空の文字列.
     */
    public static final UniCord EMPTY = new UniCord("");
    private static final int BUFFER = 64;

    /**
     *
     */
    private static final long serialVersionUID = -8414455913324762189L;

    /**
     * 正規化して比較.
     *
     * @param lpStr1 文字列1
     * @param lpStr2 文字列2
     * @return 比較結果.
     */
    public static int compareNormalized(String lpStr1, String lpStr2) {
        UniCord left = new UniCord(lpStr1);
        UniCord right = new UniCord(lpStr2);
        return left.compareNormalized(right);
    }

    /**
     * 小文字に変換して比較
     *
     * @param lhs 比較する文字列1
     * @param rhs 比較する文字列2
     * @return 一致すれば true.
     */
    public static boolean equalsIgnoreCase(UniCord lhs, UniCord rhs) {
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
    public static boolean isEmpty(UniCord str) {
        return str == null || str.source.length() == 0;
    }

    /**
     * UniCord を作成.
     *
     * @param value 元のオブジェクト.
     * @return 値
     */
    @Nonnull
    public static UniCord valueOf(Object value) {
        if (value == null) {
            return EMPTY;
        }
        else {
            return valueOf(value.toString());
        }
    }

    /**
     * UniCord を作成.
     *
     * @param value 元のオブジェクト.
     * @return 値
     */
    @Nonnull
    public static UniCord valueOf(String value) {
        if (Misc.isEmpty(value)) {
            return EMPTY;
        }
        else {
            return new UniCord(value);
        }
    }

    final UnicodeVector source;

    /**
     * コンストラクター
     *
     * @param source 元の文字列.
     */
    public UniCord(String source) {
        this(false, source);
    }

    /**
     * 正規分解可能なコンストラクター
     *
     * @param doDecompose 分解するか
     * @param source 元の文字列.
     */
    public UniCord(boolean doDecompose, String source) {
        if (doDecompose) {
            this.source = new UnicodeVector(true, normalize(source, NFD));
        }
        else {
            this.source = new UnicodeVector(source);
        }
    }

    /**
     * 正規分解して比較を行う.
     *
     *
     * @param other 比較する文字列.
     * @return 比較結果.
     */
    public int compareNormalized(UniCord other) {
        return this.decompose().compareTo(other.decompose());
    }

    @Override
    public int compareTo(UniCord o) {
        return source.compareTo(o.source);
    }

    /**
     * 文字列が存在するかどうか.
     *
     * @see UniCord#indexOf(Rope)
     *
     * @param search 検索対象
     * @return 検索対象が存在すればtrue. ただし、検索対象が空なら常にfalse.
     */
    public boolean contains(UniCord search) {
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
        return indexOf(search) >= 0;
    }

    /**
     * 文字列を正規分解した結果を戻す
     *
     * @return 文字列を正規分解した結果
     */
    @Nonnull
    public UniCord decompose() {
        if (source.isDecomposed()) {
            return this;
        }
        else {
            return new UniCord(true, source.getSource());
        }
    }

    /**
     * 指定された文字列で終了しているかどうか判断する.
     *
     * @param str 検査対象の文字列.
     * @param offset オフセット
     * @return 指定された文字列で終了していれば true.
     */
    public boolean endsWith(String str, int offset) {
        UniCord rhs = new UniCord(str).decompose();
        return endsWith(rhs.source.elements(), offset);
    }

    /**
     * 指定された文字位置にある文字を取得する.
     *
     * @param index 位置
     * @return 指定された位置の文字.
     */
    @Nonnull
    public String getAt(int index) {
        return source.elements()[index].getElement();
    }

    /**
     *
     * @return ブレイクイテレーター
     */
    @Nonnull
    public BreakIterator getBreakIterator() {
        BreakIterator iter = getCharacterInstance();
        iter.setText(source.getSource());
        return iter;
    }

    /**
     * 文字列の検索
     *
     * @param searchString 検索する文字列
     * @return 見つかった位置。見つからない場合は-1.
     */
    public int indexOf(@Nonnull UniCord searchString) {
        return indexOf(searchString, 0);
    }

    /**
     * 文字列の検索
     *
     * @param searchString 検索する文字列
     * @param offset 検索開始位置
     * @return 見つかった位置。見つからない場合は-1.
     */
    public int indexOf(@Nonnull UniCord searchString, int offset) {
        UnicodeScalar[] search = searchString.decompose().source.elements();
        for (int i = offset; i < source.elements().length; i++) {
            if (startsWith(search, i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 文字列の検索.
     *
     * @see java.lang.String#indexOf(String)
     * @param str 検索する文字列
     * @return 見つかった位置
     */
    public int indexOf(String str) {
        return indexOf(valueOf(str));
    }

    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public Iterator<String> iterator() {
        return new Iter(source.iterator());
    }

    /**
     * 指定した文字列を終端から検索
     *
     * @param searchString 検索する文字列
     * @return 文字列が見つかった先頭からの位置。見つからなければ-1
     */
    public int lastIndexOf(@Nonnull UniCord searchString) {
        UnicodeScalar[] search = searchString.decompose().source.elements();
        for (int i = source.elements().length - 1; 0 <= i; i--) {
            if (startsWith(search, i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 文字列の検索.
     *
     * @see java.lang.String#lastIndexOf(String)
     * @param str 検索する文字列
     * @return 見つかった位置
     */
    public int lastIndexOf(String str) {
        UniCord rhs = new UniCord(str);
        return lastIndexOf(rhs);
    }

    /**
     * 左から指定長の文字列を取得.
     *
     * @param length 文字数
     * @return 指定長の文字列
     */
    @Nonnull
    public String left(int length) {
        return source.subVector(0, length).toString();
    }

    /**
     * 結合文字まで含めた正確な文字数を取得する.
     *
     * @return 文字数
     */
    public int length() {
        return source.elements().length;
    }

    /**
     * 文字列置換
     *
     * @see java.lang.String#replaceAll(String, String) とは異なり、正規表現を利用しない.
     *
     * @param searchString 置換対象の文字列
     * @param replacement 置換後の文字列
     * @param max 置換する数。マイナスの値で全て
     * @return 置換された文字列
     */
    @Nonnull
    public UniCord replace(UniCord searchString, UniCord replacement, int max) {
        if (isEmpty(searchString) || replacement == null || max == 0) {
            return this;
        }

        searchString = searchString.decompose();

        int start = 0;
        int end = indexOf(searchString, start);
        if (end == -1) {
            return this;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? BUFFER : max > BUFFER ? BUFFER : max;
        StringBuilder buf = new StringBuilder(source.length() + increase);
        while (end != -1) {
            buf.append(substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = indexOf(searchString, start);
        }
        buf.append(substring(start));
        return new UniCord(buf.toString());
    }

    /**
     * 文字列置換
     *
     * @see java.lang.String#replaceAll(String, String) とは異なり、正規表現を利用しない.
     *
     * @param searchString 置換対象の文字列
     * @param replacement 置換後の文字列
     * @return 置換された文字列
     */
    @Nonnull
    public UniCord replaceAll(UniCord searchString, UniCord replacement) {
        return replace(searchString, replacement, -1);
    }

    @Nonnull
    public Iterator<String> reverseIterator() {
        return new Iter(source.reverseIterator());
    }

    /**
     * 右から指定長の文字列を取得.
     *
     * @param length 文字数
     * @return 指定長の文字列
     */
    @Nonnull
    public String right(int length) {
        return source.subVector(source.elements().length - length).toString();
    }

    /**
     * 指定された文字列から開始するか.
     *
     * @param str 文字列
     * @param offset オフセット
     * @return 開始しているなら true.
     */
    public boolean startsWith(String str, int offset) {
        UniCord rhs = new UniCord(str).decompose();
        return startsWith(rhs.source.elements(), offset);
    }

    /**
     * 文字列の切り出し.
     *
     * @param start 開始位置
     * @return [開始位置,文字列長)の文字列
     */
    @Nonnull
    public String substring(int start) {
        return substring(start, length());
    }

    /**
     * 文字列の切り出し
     *
     * @param start 開始位置
     * @param end 終了位置
     * @return [開始位置,終了位置)の文字列
     */
    @Nonnull
    public String substring(int start, int end) {
        return source.subVector(start, end).toString();
    }

    /**
     * すべて小文字に変換
     *
     * @return すべて小文字に変換した文字列.
     */
    @Nonnull
    public UniCord toLowerCase() {
        String lc = source.getSource().toLowerCase(Locale.getDefault());
        return new UniCord(source.isDecomposed(), lc);
    }

    /**
     * すべて小文字に変換
     *
     * @param locale ロケール
     * @return すべて小文字に変換した文字列
     */
    @Nonnull
    public UniCord toLowerCase(Locale locale) {
        String lc = source.getSource().toLowerCase(locale);
        return new UniCord(source.isDecomposed(), lc);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return source.getSource();
    }

    /**
     * すべて小文字に変換
     *
     * @return すべて小文字に変換した文字列.
     */
    @Nonnull
    public UniCord toUpperCase() {
        String uc = source.getSource().toUpperCase(Locale.getDefault());
        return new UniCord(source.isDecomposed(), uc);
    }

    /**
     * すべて小文字に変換
     *
     * @param locale
     * @return すべて小文字に変換した文字列.
     */
    @Nonnull
    public UniCord toUpperCase(Locale locale) {
        String uc = source.getSource().toUpperCase(locale);
        return new UniCord(source.isDecomposed(), uc);
    }

    private boolean endsWith(UnicodeScalar[] search, int offset) {
        int len = search.length;
        int idx = source.elements().length - offset - len;
        if (idx < 0) {
            return false;
        }
        return arrayCompareTo(source.elements(), idx, len, search, 0, len) == 0;
    }

    private boolean startsWith(UnicodeScalar[] search, int offset) {
        int len = search.length;
        int idx = source.elements().length - offset - len;
        if (idx < 0) {
            return false;
        }
        return arrayCompareTo(source.elements(), offset, len, search, 0, len) == 0;
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

}
