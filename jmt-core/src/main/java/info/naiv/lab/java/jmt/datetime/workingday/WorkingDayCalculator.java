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
package info.naiv.lab.java.jmt.datetime.workingday;

import info.naiv.lab.java.jmt.Constants;
import info.naiv.lab.java.jmt.datetime.CalendarIterationUnits;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.iteration.IterationUnitIterator;
import static info.naiv.lab.java.jmt.iteration.IterationUtils.count;
import static info.naiv.lab.java.jmt.iteration.IterationUtils.filter;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import java.util.Iterator;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 * @author enlo
 */
public class WorkingDayCalculator {

    private final Predicate1<Calendar> workingDayPredicate = new Predicate1<Calendar>() {
        @Override
        public boolean test(Calendar obj) {
            return settings.isWorkingDay(obj);
        }
    };

    @Getter
    @Setter
    @NonNull
    WorkingDaySettings settings;

    public WorkingDayCalculator() {
        this.settings = WorkingDaySettings.newInstance();
    }

    public WorkingDayCalculator(WorkingDaySettings settings) {
        this.settings = settings;
    }

    /**
     * 営業日計算.
     * 指定された日付が休日の場合、{@link #shiftIfHoliday(java.util.Calendar, info.naiv.java.jmt.datetime.WorkingDaySettings) }
     * を呼び出し、営業日にしてから処理を行う
     *
     * @param cal 日付
     * @param days 加算する営業日.
     * @return 営業日分だけ加算した値.
     */
    @Nonnull
    public Calendar addWorkingDays(@NonNull Calendar cal, int days) {
        int mod = days < 0 ? -1 : 1;
        int absDays = mod * days;
        Calendar result = shiftIfHoliday(cal);
        for (int i = 0; i < absDays; i++) {
            result.add(DAY_OF_MONTH, mod);
            while (settings.isHoliday(result)) {
                result.add(DAY_OF_MONTH, mod);
            }
        }
        return result;
    }

    /**
     * 指定された週の最初の営業日を取得する.週の開始日が休日の場合、翌平日を戻す.
     *
     * @param cal カレンダー
     * @return 週の最初の営業日.
     */
    @Nonnull
    public Calendar computeFirstBizDayOfWeek(@NonNull Calendar cal) {
        Calendar base = (Calendar) cal.clone();
        int fdow = settings.getWeekSettings().getFirstDayOfWeek();
        int dow = base.get(DAY_OF_WEEK);
        int mod = (dow < fdow) ? Constants.SEVEN_DAYS : 0;
        base.setFirstDayOfWeek(fdow);
        base.add(DAY_OF_MONTH, fdow - dow - mod);
        while (settings.isHoliday(base)) {
            base.add(DAY_OF_MONTH, 1);
        }
        return base;
    }

    /**
     * 期間の営業日を計算する. 時間は切り捨てする. 期間開始日と期間終了日が同じ日付の場合、0日として戻す.
     *
     * @param from
     * @param to
     * @return
     */
    public int countWorkingDays(@NonNull Calendar from, @NonNull Calendar to) {
        CalendarIterationUnits iu = CalendarIterationUnits.DAY1;
        Calendar fd = iu.truncate(from);
        Calendar td = iu.truncate(to);
        int cr = fd.compareTo(td);
        if (cr == 0) {
            return 0;
        }
        else if (cr < 0) {
            Iterator<Calendar> it = new IterationUnitIterator<>(fd, td, iu);
            return (int) count(filter(it, workingDayPredicate));
        }
        else {
            Iterator<Calendar> it = new IterationUnitIterator<>(td, fd, iu);
            return (int) -count(filter(it, workingDayPredicate));
        }
    }

    /**
     * 日付が休日なら、営業日までシフトした日付を戻し、<br>
     * そうでなければカレンダーを戻す.
     *
     * @param cal
     * @return
     */
    @Nonnull
    public Calendar shiftIfHoliday(@NonNull Calendar cal) {
        Calendar result = (Calendar) cal.clone();
        int shift = settings.isShiftForward() ? 1 : -1;
        while (settings.isHoliday(result)) {
            result.add(DAY_OF_MONTH, shift);
        }
        return result;
    }

}
