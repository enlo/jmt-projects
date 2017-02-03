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

import info.naiv.lab.java.jmt.Misc;
import java.util.Calendar;
import javax.annotation.Nonnull;

/**
 * 日付時刻マスク.
 *
 * @author enlo
 */
public enum DateTimeMask {

    /**
     * マスクしない.
     */
    NO_MASK {
        @Override
        public Calendar mask(Calendar date) {
            return date;
        }
    },
    /**
     * 時刻をマスク.
     */
    TIME_MASK {
        @Override
        public Calendar mask(Calendar date) {
            return ClassicDateUtils.clearTimePart((Calendar) date.clone());
        }
    },
    /**
     * 日付をマスク.
     */
    DATE_MASK {
        @Override
        public Calendar mask(Calendar date) {
            return ClassicDateUtils.clearDatePart((Calendar) date.clone());
        }
    };

    /**
     * 範囲比較.
     *
     * @param target 検査する日付
     * @param from 範囲開始
     * @param to 範囲終了
     * @return 範囲内なら true.
     */
    public boolean between(Calendar target, Calendar from, Calendar to) {
        Calendar maskedTarget = mask(target);
        Calendar maskedFrom = mask(from);
        Calendar maskedTo = mask(to);
        return Misc.between(maskedTarget, maskedFrom, maskedTo);
    }

    /**
     *
     * @param date
     * @return
     */
    @Nonnull
    public abstract Calendar mask(Calendar date);

}
