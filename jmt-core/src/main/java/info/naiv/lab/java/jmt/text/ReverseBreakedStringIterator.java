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

import java.text.BreakIterator;
import static java.text.BreakIterator.DONE;
import java.util.Iterator;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class ReverseBreakedStringIterator implements Iterator<String> {

    final BreakIterator iter;
    final String source;

    /**
     * Constructor.
     *
     * @param source 元の文字列
     * @param iter BreakIterator
     */
    public ReverseBreakedStringIterator(@Nonnull String source, @Nonnull BreakIterator iter) {
        this.source = source;
        this.iter = iter;
    }

    @Override
    public boolean hasNext() {
        boolean result = iter.previous() != DONE;
        if (result) {
            iter.next();
        }
        return result;
    }

    @Override
    public String next() {
        int t = iter.current();
        int h = iter.previous();
        String result = source.substring(h, t);
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
