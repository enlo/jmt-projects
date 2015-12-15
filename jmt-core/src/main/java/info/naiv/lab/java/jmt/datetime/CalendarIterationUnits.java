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

import info.naiv.lab.java.jmt.IterationUnit;
import info.naiv.lab.java.jmt.Misc;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author enlo
 */
public enum CalendarIterationUnits implements IterationUnit<Calendar> {

    MILLSEC1(Calendar.MILLISECOND, 1),
    MILLSEC10(Calendar.MILLISECOND, 10),
    MILLSEC100(Calendar.MILLISECOND, 100),
    MILLSEC500(Calendar.MILLISECOND, 500),
    SEC1(Calendar.SECOND, TimeUnit.SECONDS.toMillis(1)),
    SEC10(Calendar.SECOND, TimeUnit.SECONDS.toMillis(10)),
    SEC30(Calendar.SECOND, TimeUnit.SECONDS.toMillis(30)),
    MIN1(Calendar.MINUTE, TimeUnit.MINUTES.toMillis(1)),
    MIN5(Calendar.MINUTE, TimeUnit.MINUTES.toMillis(5)),
    MIN10(Calendar.MINUTE, TimeUnit.MINUTES.toMillis(10)),
    MIN15(Calendar.MINUTE, TimeUnit.MINUTES.toMillis(15)),
    MIN30(Calendar.MINUTE, TimeUnit.MINUTES.toMillis(30)),
    HOUR1(Calendar.HOUR, TimeUnit.HOURS.toMillis(1)),
    HOUR2(Calendar.HOUR, TimeUnit.HOURS.toMillis(2)),
    HOUR3(Calendar.HOUR, TimeUnit.HOURS.toMillis(3)),
    HOUR4(Calendar.HOUR, TimeUnit.HOURS.toMillis(4)),
    HOUR6(Calendar.HOUR, TimeUnit.HOURS.toMillis(6)),
    HOUR8(Calendar.HOUR, TimeUnit.HOURS.toMillis(8)),
    HOUR12(Calendar.HOUR, TimeUnit.HOURS.toMillis(12)),
    DAY1(Calendar.DAY_OF_MONTH, TimeUnit.DAYS.toMillis(1)),
    WEEK1(Calendar.WEEK_OF_MONTH, 1) {

                @Override
                protected Calendar truncateCore(Calendar cal) {
                    return truncateWeek(cal, multi);
                }

                @Override
                protected Calendar advanceCore(Calendar c, long x) {
                    return advanceWeek(c, multi * x);
                }

                @Override
                protected long distanceCore(Calendar x, Calendar y) {
                    return distanceWeek(x, y, multi);
                }

            },
    WEEK4(Calendar.WEEK_OF_MONTH, 4) {

                @Override
                protected Calendar truncateCore(Calendar cal) {
                    return truncateWeek(cal, multi);
                }

                @Override
                protected Calendar advanceCore(Calendar c, long x) {
                    return advanceWeek(c, multi * x);
                }

                @Override
                protected long distanceCore(Calendar x, Calendar y) {
                    return distanceWeek(x, y, multi);
                }

            },
    WEEK6(Calendar.WEEK_OF_MONTH, 6) {

                @Override
                protected Calendar truncateCore(Calendar cal) {
                    return truncateWeek(cal, multi);
                }

                @Override
                protected Calendar advanceCore(Calendar c, long x) {
                    return advanceWeek(c, multi * x);
                }

                @Override
                protected long distanceCore(Calendar x, Calendar y) {
                    return distanceWeek(x, y, multi);
                }

            },
    MONTH1(Calendar.MONTH, 1) {

                @Override
                protected Calendar truncateCore(Calendar cal) {
                    return truncateMonth(cal, multi);
                }

                @Override
                protected Calendar advanceCore(Calendar c, long x) {
                    return truncateMonth(c, multi * x);
                }

                @Override
                protected long distanceCore(Calendar x, Calendar y) {
                    return distanceMonth(x, y, multi);
                }
            },
    MONTH3(Calendar.MONTH, 3) {

                @Override
                protected Calendar truncateCore(Calendar cal) {
                    return truncateMonth(cal, multi);
                }

                @Override
                protected Calendar advanceCore(Calendar c, long x) {
                    return truncateMonth(c, multi * x);
                }

                @Override
                protected long distanceCore(Calendar x, Calendar y) {
                    return distanceMonth(x, y, multi);
                }
            },
    MONTH6(Calendar.MONTH, 6) {

                @Override
                protected Calendar truncateCore(Calendar cal) {
                    return truncateMonth(cal, multi);
                }

                @Override
                protected Calendar advanceCore(Calendar c, long x) {
                    return truncateMonth(c, multi * x);
                }

                @Override
                protected long distanceCore(Calendar x, Calendar y) {
                    return distanceMonth(x, y, multi);
                }
            },;

    int calendarType;
    long multi;

    private CalendarIterationUnits(int calendarType, long multi) {
        this.calendarType = calendarType;
        this.multi = multi;
    }

    protected Calendar advanceCore(Calendar c, long x) {
        return ClassicDateUtils.add(c, multi * x, TimeUnit.MILLISECONDS);
    }

    protected static Calendar advanceWeek(Calendar c, long x) {
        int w = c.get(Calendar.WEEK_OF_YEAR);
        c.set(Calendar.WEEK_OF_YEAR, (int) (w + x));
        return c;
    }

    protected static Calendar advanceMonth(Calendar c, long x) {
        int w = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, (int) (w + x));
        return c;
    }

    protected long distanceCore(Calendar x, Calendar y) {
        long d = y.getTimeInMillis() - x.getTimeInMillis();
        long r = d / multi + (d % multi != 0 ? 1 : 0);
        return r;
    }

    @Override
    public final Calendar truncate(Calendar cal) {
        return truncateCore((Calendar) cal.clone());
    }

    protected Calendar truncateCore(Calendar cal) {
        return truncateMills(cal, multi);
    }

    protected static Calendar truncateMills(Calendar cal, long x) {
        cal.setTimeInMillis((cal.getTimeInMillis() / x) * x);
        return cal;
    }

    protected static Calendar truncateDays(Calendar cal, long x) {
        truncateMills(cal, TimeUnit.DAYS.toMillis(x));
        return cal;
    }

    protected static Calendar truncateWeek(Calendar cal, long x) {
        cal = truncateDays(cal, 1);
        int year = cal.get(Calendar.YEAR);
        int week = (int) ((cal.get(Calendar.WEEK_OF_YEAR) / x) * x);
        cal.setWeekDate(year, week, Calendar.SUNDAY);
        return cal;
    }

    protected static Calendar truncateMonth(Calendar cal, long x) {
        cal = truncateDays(cal, 1);
        int month = (int) ((cal.get(Calendar.MONTH) / x) * x);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal;
    }

    protected static long distanceWeek(Calendar lhs, Calendar rhs, long x) {
        if (lhs.equals(rhs)) {
            return 0;
        }
        long unit = TimeUnit.DAYS.toMillis(7);
        long dist = (rhs.getTimeInMillis() - lhs.getTimeInMillis()) / unit;
        return (dist / x) + (dist % x != 0 ? 1 : 0);
    }

    protected static long distanceMonth(Calendar lhs, Calendar rhs, long x) {
        if (lhs.equals(rhs)) {
            return 0;
        }
        long minus = rhs.compareTo(lhs) <= 0 ? -1 : 1;
        Calendar[] mc = Misc.sort(lhs, rhs);
        int dist = (mc[1].get(Calendar.YEAR) - mc[0].get(Calendar.YEAR)) * 12;
        mc[0].add(Calendar.MONTH, dist);
        dist -= (mc[1].get(Calendar.MONTH) - mc[0].get(Calendar.MONTH));
        return ((dist / x) + (dist % x != 0 ? 1 : 0)) * minus;
    }

    @Override
    public final Calendar advance(Calendar cal, long x) {
        Calendar c = truncate(cal);
        return advanceCore(c, x);
    }

    @Override
    public final Calendar next(Calendar value) {
        return advance(value, 1);
    }

    @Override
    public final Calendar prior(Calendar value) {
        return advance(value, -1);
    }

    @Override
    public final long distance(Calendar lhs, Calendar rhs) {
        Calendar x = truncate(lhs);
        Calendar y = truncate(rhs);
        return distanceCore(x, y);
    }

    @Override
    public final int compare(Calendar o1, Calendar o2) {
        return truncate(o1).compareTo(truncate(o2));
    }

}
