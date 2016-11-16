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
package info.naiv.lab.java.jmt.datetime;

import info.naiv.lab.java.jmt.fx.Predicate1;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class SameDayPredicate implements Predicate1<Calendar> {

    final Calendar value;

    /**
     *
     * @param value
     */
    public SameDayPredicate(Calendar value) {
        this.value = value;
    }

    /**
     *
     * @param a1
     * @return
     */
    @Override
    @Nonnull
    public boolean test(Calendar a1) {
        if (value == a1) {
            return true;
        }
        else {
            return value.get(YEAR) == a1.get(YEAR)
                    && value.get(DAY_OF_YEAR) == a1.get(DAY_OF_YEAR);
        }
    }

}
