package info.naiv.lab.java.jmt.text;

import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayEqualsInRange;
import info.naiv.lab.java.jmt.Misc;
import java.io.Serializable;
import java.util.*;
import static java.util.Arrays.copyOf;
import javax.annotation.Nonnull;
import lombok.EqualsAndHashCode;

/**
 *
 * @author enlo
 */
@EqualsAndHashCode(of = "origin", callSuper = false)
public class CodePoints extends AbstractCollection<Integer> implements Collection<Integer>, Serializable, Comparable<CodePoints> {

    private static final long serialVersionUID = 1L;

    final int[] codePoints;
    final int[] index;
    final String origin;

    public CodePoints() {
        this("");
    }

    public CodePoints(String source) {
        if (Misc.isEmpty(source)) {
            this.origin = "";
            this.codePoints = new int[0];
            this.index = new int[0];
        }
        else {
            int len = source.length();
            int size = source.codePointCount(0, len);
            this.origin = source;
            this.codePoints = new int[size];
            this.index = new int[size];
            for (int i = 0, j = 0; i < len; i = source.offsetByCodePoints(i, 1), j++) {
                codePoints[j] = source.codePointAt(i);
                index[j] = i;
            }
        }
    }

    public int codePointAt(int i) {
        return codePoints[i];
    }

    @Override
    public int compareTo(CodePoints o) {
        return origin.compareTo(o.origin);
    }

    /**
     * 文字列が存在するかどうか.
     *
     * @see CodePoints#indexOf(CodePoints)
     *
     * @param search 検索対象
     * @return 検索対象が存在すればtrue. ただし、検索対象が空なら常にfalse.
     */
    public boolean contains(CodePoints search) {
        if (Misc.isEmpty(search)) {
            return false;
        }
        return indexOf(search) >= 0;
    }

    /**
     * 文字列の検索
     *
     * @param searchString 検索する文字列
     * @return 見つかった位置。見つからない場合は-1.
     */
    public int indexOf(CodePoints searchString) {
        return indexOf(searchString, 0);
    }

    /**
     * 文字列の検索
     *
     * @param searchString 検索する文字列
     * @param offset 検索開始位置
     * @return 見つかった位置。見つからない場合は-1.
     */
    public int indexOf(CodePoints searchString, int offset) {
        if (searchString != null) {
            int tl = size();
            int sl = searchString.size();
            if (sl == 0) {
                return 0;
            }
            for (int i = offset; i < tl; i++) {
                if (size() - i < sl) {
                    break;
                }
                if (isMatch(searchString.codePoints, i)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new CodePointIterator();
    }

    @Override
    public int size() {
        return codePoints.length;
    }

    @Nonnull
    public String stringAt(int i) {
        int from = getBeginIndex(i);
        int to = getEndIndex(i);
        return origin.substring(from, to);
    }

    @Nonnull
    public String subCodePoints(int from, int to) {
        from = getBeginIndex(from);
        to = getEndIndex(to);
        return origin.substring(from, to);
    }

    @Nonnull
    public int[] toCodePointArray() {
        return copyOf(codePoints, codePoints.length);
    }

    @Override
    public String toString() {
        return origin;
    }

    private int getBeginIndex(int i) {
        return index[i];
    }

    private int getEndIndex(int i) {
        int n = i + 1;
        if (n < codePoints.length) {
            return getBeginIndex(n);
        }
        else {
            return origin.length();
        }
    }

    private boolean isMatch(int[] searchString, int offset) {
        return arrayEqualsInRange(codePoints, offset, searchString, 0, searchString.length);
    }

    class CodePointIterator implements Iterator<Integer> {

        int i = 0;

        @Override
        public boolean hasNext() {
            return i < codePoints.length;
        }

        @Override
        public Integer next() {
            return codePoints[i++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
