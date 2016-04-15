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
package info.naiv.lab.java.jmt.datetime;

import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.createDate;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.getDatePart;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.now;
import java.util.Date;

/**
 *
 * @author enlo
 */
public class DateOnly extends java.sql.Date {

    private static final long serialVersionUID = 1L;

    public DateOnly() {
        this(now().getTime());
    }

    public DateOnly(int year, int month, int day) {
        super(createDate(year, month, day).getTime());
    }

    public DateOnly(long date) {
        this(new Date(date));
    }

    public DateOnly(Date date) {
        super(getDatePart(date).getTime());
    }

    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    public DateOnly clone() {
        return (DateOnly) super.clone();
    }

    @Override
    public void setTime(long date) {
        Date newValue = new DateOnly(date);
        super.setTime(newValue.getTime());
    }

}
