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

import info.naiv.lab.java.jmt.range.Range;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author enlo
 * @param <T>
 */
@Data
@NoArgsConstructor
public class SimpleRangeLimiter<T extends Comparable<T>> implements RangeLimiter<T> {

    T max;
    T min;

    public SimpleRangeLimiter(@Nonnull Range<T> range) {
        min = range.getMinValue();
        max = range.getMaxValue();
    }

    public SimpleRangeLimiter(T min, T max) {
        this.min = min;
        this.max = max;
    }

    @Override
    @CheckReturnValue
    public T limit(T value) {
        return Misc.minmax(value, min, max);
    }

}
