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

import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Misc.contains;
import java.io.Serializable;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_WEEK;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

/**
 *
 * @author enlo
 */
@EqualsAndHashCode
@ToString
public class WorkingDaySettings implements Cloneable, Serializable {

    private static final long serialVersionUID = 612688843813733186L;

    public static WorkingDaySettings newInstance() {
        return new WorkingDaySettings(WeekSettings.DEFAULT,
                                      new HashSet<Calendar>(),
                                      new HashSet<Calendar>(), true);
    }

    @NonNull
    private Set<Calendar> extractHolidays;

    @NonNull
    private Set<Calendar> holidays;

    private boolean shiftForward;

    @NonNull
    private WeekSettings weekSettings;

    public WorkingDaySettings(WeekSettings weekSettings,
                              Set<Calendar> holidays,
                              Set<Calendar> extractHolidays,
                              boolean shiftForward) {
        this.weekSettings = weekSettings;
        this.holidays = holidays;
        this.extractHolidays = extractHolidays;
        this.shiftForward = shiftForward;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Nonnull
    public Set<Calendar> getExtractHolidays() {
        return extractHolidays;
    }

    @Nonnull
    public Set<Calendar> getHolidays() {
        return holidays;
    }

    @Nonnull
    public WeekSettings getWeekSettings() {
        return weekSettings;
    }

    /**
     * 休日かどうか判断する. <br>
     * 休日例外セットに存在せず、休日曜日または休日セットにあれば true.
     *
     * @param cal
     * @return 休日ならば true.
     */
    public boolean isHoliday(Calendar cal) {
        SameDayPredicate predicate = new SameDayPredicate(cal);
        if (contains(extractHolidays, predicate)) {
            return false;
        }
        return contains(holidays, predicate) || weekSettings.isHoliday(cal.get(DAY_OF_WEEK));
    }

    /**
     * 営業日を計算する場合に、翌日以降にシフトするかどうか.
     *
     * @return 営業日を計算する場合に、翌日以降にシフトする場合は true.<br>
     * false の場合は前日以前にシフトする.
     */
    public boolean isShiftForward() {
        return shiftForward;
    }

    /**
     * 平日かどうか判断する.
     *
     * @param cal カレンダー
     * @return 休日で無ければ true.
     */
    public boolean isWorkingDay(Calendar cal) {
        return !isHoliday(cal);
    }

    public void setExtractHolidays(Set<Calendar> extractHolidays) {
        this.extractHolidays = extractHolidays;
    }

    public void setHolidays(Set<Calendar> holidays) {
        this.holidays = holidays;
    }

    public void setShiftForward(boolean shiftForward) {
        this.shiftForward = shiftForward;
    }

    public void setWeekSettings(WeekSettings weekSettings) {
        nonNull(weekSettings, "weekSettings");
        this.weekSettings = weekSettings;
    }

}
