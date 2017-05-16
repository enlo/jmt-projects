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

import info.naiv.lab.java.jmt.IterationUnit;
import info.naiv.lab.java.jmt.iteration.IterationUnitIterator;
import info.naiv.lab.java.jmt.iteration.StandardIterationUnits;
import java.io.Serializable;
import java.util.Iterator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;

/**
 *
 * @author enlo
 * @param <T>
 */
@EqualsAndHashCode
@ToString
public class Range<T extends Comparable<T>> implements Cloneable, Serializable {

    private static final long serialVersionUID = 2520394186992026035L;

    @Getter
    protected final Bound<T> lowerBound;

    @Getter
    protected final IterationUnit<? super T> unit;

    @Getter
    protected final Bound<T> upperBound;

    /**
     * コンストラクター
     *
     * @param unit 単位.
     * @param lowerBound 下限.
     * @param upperBound 上限.
     */
    public Range(IterationUnit<? super T> unit, @NonNull Bound<T> lowerBound, @NonNull Bound<T> upperBound) {
        if (unit != null) {
            this.unit = unit;
        }
        else {
            this.unit = StandardIterationUnits.COMPARE_ONLY;
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     *
     * @see #getLowerBound()
     * @see Bound#getValue()
     * @return 下限設定値.
     */
    public T begin() {
        return lowerBound.getValue();
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    @SneakyThrows
    public Range<T> clone() {
        return (Range<T>) super.clone();
    }

    /**
     *
     * @see #getUpperBound()
     * @see Bound#getValue()
     * @return 上限設定値.
     */
    public T end() {
        return upperBound.getValue();
    }

    /**
     * 許容される最大の値を戻す.
     *
     * @return 許容される最大の値.
     */
    public T getMaxValue() {
        T val = upperBound.getValue();
        return upperBound.isOpen() ? (T) unit.prior(val) : val;
    }

    /**
     * 許容される最小の値を戻す.
     *
     * @return 許容される最大の値.
     */
    public T getMinValue() {
        T val = lowerBound.getValue();
        return lowerBound.isOpen() ? (T) unit.next(val) : val;
    }

    public Range<T> newLowerBound(T newValue) {
        Bound<T> newLb = lowerBound.construct(newValue);
        return new Range<>(unit, newLb, upperBound);
    }

    public Range<T> newUnit(IterationUnit<T> newUnit) {
        return new Range<>(newUnit, lowerBound, upperBound);
    }

    public Range<T> newUpperBound(T newValue) {
        Bound<T> newUb = upperBound.construct(newValue);
        return new Range<>(unit, lowerBound, newUb);
    }

    /**
     * 反復処理.
     *
     * @return
     */
    public Iterable<T> toIterable() {
        final T from = getMinValue();
        final T to = getMaxValue();
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new IterationUnitIterator<>(from, to, unit);
            }
        };
    }

    /**
     * 範囲内に存在するか.
     *
     * @param value 値
     * @return 範囲内なら true.
     */
    boolean contains(T value) {
        return lowerBound.on(value, unit) && upperBound.on(value, unit);
    }

}
