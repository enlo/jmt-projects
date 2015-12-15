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
package info.naiv.lab.java.jmt.range;

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import info.naiv.lab.java.jmt.IterationUnit;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author enlo
 * @param <T>
 */
@EqualsAndHashCode
@ToString
public class Range<T extends Comparable<T>> implements Cloneable, Serializable {

    @Getter
    protected IterationUnit<T> unit;

    @Getter
    protected final Bound<T> lowerBound;

    @Getter
    protected final Bound<T> upperBound;

    public Range(IterationUnit<T> unit, Bound<T> lowerBound, Bound<T> upperBound) {
        nonNull(lowerBound, "lowerBound");
        nonNull(upperBound, "upperBound");

        this.unit = unit;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    boolean contains(T value) {
        return lowerBound.on(value, unit) && upperBound.on(value, unit);
    }

    public T begin() {
        return lowerBound.getValue();
    }

    public T end() {
        return upperBound.getValue();
    }

    public T getMinValue() {
        checkUnit();
        T val = lowerBound.getValue();
        return lowerBound.isOpen() ? unit.next(val) : val;
    }

    public T getMaxValue() {
        checkUnit();
        T val = upperBound.getValue();
        return upperBound.isOpen() ? unit.prior(val) : val;
    }

    private void checkUnit() throws UnsupportedOperationException {
        if (unit == null) {
            throw new UnsupportedOperationException("unit is null.");
        }
    }

    public Range<T> newUnit(IterationUnit<T> newUnit) {
        return new Range<>(newUnit, lowerBound, upperBound);
    }

    public Range<T> newUpperBound(T newValue) {
        Bound<T> newUb = upperBound.construct(newValue);
        return new Range<>(unit, lowerBound, newUb);
    }

    public Range<T> newLowerBound(T newValue) {
        Bound<T> newLb = lowerBound.construct(newValue);
        return new Range<>(unit, newLb, upperBound);
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public Range<T> clone() {
        try {
            return (Range<T>) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }

}
