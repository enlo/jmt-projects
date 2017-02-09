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

import info.naiv.lab.java.jmt.Lazy;
import java.io.Serializable;
import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
@ThreadSafe
@EqualsAndHashCode(of = "element")
public final class UnicodeScalar implements CharSequence, Comparable<UnicodeScalar>, Serializable, Cloneable {

    private static final UnicodeScalar[] PREPARED = new UnicodeScalar[0xFF];

    private static final long serialVersionUID = -2284780948159534496L;

    static {
        for (int c = 1; c < 0xFF; c++) {
            PREPARED[c] = new UnicodeScalar(true, String.valueOf(c));
        }
    }

    public static UnicodeScalar valueOf(CharSequence source, int i, int j) {
        if (i == j) {
            char ch = source.charAt(i);
            if (0 <= ch && ch < 0xFF) {
                return PREPARED[ch];
            }
        }
        return new UnicodeScalar(source.subSequence(i, j).toString());
    }

    private final Lazy<String> decomposed;
    @Getter
    private final String element;

    UnicodeScalar(@NonNull final String element) {
        this(false, element);
    }

    UnicodeScalar(boolean decomposed, @NonNull String element) {
        this.element = element;
        this.decomposed = decomposed ? new Lazy<>(element) : new Decomp();
    }

    @Override
    public char charAt(int index) {
        return element.charAt(index);
    }

    @Override
    public UnicodeScalar clone() {
        try {
            return (UnicodeScalar) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

    @Override
    public int compareTo(UnicodeScalar o) {
        return decomposed.get().compareTo(o.decomposed.get());
    }

    @Nonnull
    public String getDecomposed() {
        return decomposed.get();
    }

    @Override
    public int length() {
        return element.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return element.subSequence(start, end);
    }

    @Override
    public String toString() {
        return element;
    }

    class Decomp extends Lazy<String> {

        @Override
        public String initialValue() {
            return normalize(element, NFD);
        }
    }

}
