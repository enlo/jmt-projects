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

import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getDatePart;
import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.util.Calendar;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
 * @author enlo
 */
@ThreadSafe
public class DefaultCurrentDateProvider
        implements CurrentDateProvider, SystemComponent {

    @Override
    public long getNanoTime() {
        return System.nanoTime();
    }

    @Override
    public Calendar getNow() {
        return Calendar.getInstance();
    }

    @Override
    public Calendar getToday() {
        Calendar cal = Calendar.getInstance();
        return getDatePart(cal);
    }

}
