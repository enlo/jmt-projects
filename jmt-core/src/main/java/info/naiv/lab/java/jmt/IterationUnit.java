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
package info.naiv.lab.java.jmt;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author enlo
 * @param <T>
 */
public interface IterationUnit<T> extends Comparator<T>, Serializable {

    /**
     * 切り捨て.
     *
     * @param value 切り捨てする値. value は変更されない.
     * @return 切り捨てられた値.
     */
    T truncate(T value);

    /**
     * 次の値.
     *
     * @param value もとになる値. value は変更されない.
     * @return 次の値.
     */
    T next(T value);

    /**
     * 前の値.
     *
     * @param value もとになる値. value は変更されない.
     * @return 前の値.
     */
    T prior(T value);

    /**
     *
     * @param value
     * @param n
     * @return
     */
    T advance(T value, long n);

    /**
     *
     * @param lhs
     * @param rhs
     * @return
     */
    long distance(T lhs, T rhs);
}
