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

import static info.naiv.lab.java.jmt.Arguments.nonMinus;
import info.naiv.lab.java.jmt.infrastructure.component.SystemComponent;
import java.util.Calendar;

/**
 * 2桁年の短縮年を、4桁の年に変換する.<br>
 * 変換ルールは {@link SimpleDateFormat} のルールに従う。<br>
 * 基準年-80年から基準年+20年に収まるように調整する。<br>
 * マイナスの値については{@link IllegalArgumentException}を出す.
 *
 * @author enlo
 */
public class DefaultShortYearConverter
        implements ShortYearConverter, SystemComponent {

    @Override
    public int convert(int shortYear, Calendar baseDate) throws IllegalArgumentException {
        nonMinus(shortYear, "shortYear");

        int year = baseDate.get(Calendar.YEAR);
        int baseYear = year % 100;
        int from = baseYear - 80;
        int to = baseYear + 20;
        if (100 <= shortYear) {
            return shortYear;
        }
        else if (from <= shortYear && shortYear <= to) {
            return (year - baseYear) + shortYear;
        }
        else {
            return (year - baseYear - 100) + shortYear;
        }
    }

}
