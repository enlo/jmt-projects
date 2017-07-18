package info.naiv.lab.java.jmt.datetime;

import static info.naiv.lab.java.jmt.Arguments.between;
import static info.naiv.lab.java.jmt.Misc.isEmpty;
import static info.naiv.lab.java.jmt.Misc.toCalendar;
import info.naiv.lab.java.jmt.infrastructure.ServiceProviders;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.ERA;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class ClassicDateUtils {

    /**
     *
     */
    public static final int[] DATE_PART_FIELDS = {YEAR, MONTH, DAY_OF_MONTH};

    /**
     *
     */
    public static final Calendar LOCAL_EPOC_CALENDAR;

    /**
     *
     */
    public static final Date LOCAL_EPOC_DATE;
    /**
     *
     */
    public static final int[] TIME_PART_FIELDS = {HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND};
    public static final TimeZone TZ_UTC = TimeZone.getTimeZone("UTC");

    /**
     *
     */
    public static final Calendar UTC_EPOC_CALENDAR;

    /**
     *
     */
    public static final Date UTC_EPOC_DATE;

    static {
        LOCAL_EPOC_DATE = Timestamp.valueOf("1970-01-01 00:00:00");
        LOCAL_EPOC_CALENDAR = toCalendar(LOCAL_EPOC_DATE, null);

        UTC_EPOC_CALENDAR = Calendar.getInstance(TZ_UTC);
        UTC_EPOC_CALENDAR.setTimeInMillis(0);
        UTC_EPOC_DATE = UTC_EPOC_CALENDAR.getTime();
    }

    /**
     * 日付を加算する. 既存のオブジェクトは変更しない.
     *
     * @param date 日付
     * @param amount 加算する値
     * @param timeUnit 単位
     * @return 結果
     */
    @Nonnull
    public static Date add(@NonNull Date date, long amount, @NonNull TimeUnit timeUnit) {
        return new Date(date.getTime() + timeUnit.toMillis(amount));
    }

    /**
     * 日付を加算する. 既存のオブジェクトは変更しない.
     *
     * @param cal カレンダー
     * @param amount 加算する値
     * @param timeUnit 単位
     * @return 結果
     */
    @Nonnull
    public static Calendar add(@NonNull Calendar cal, long amount, @NonNull TimeUnit timeUnit) {
        Calendar c = (Calendar) cal.clone();
        c.setTimeInMillis(cal.getTimeInMillis() + timeUnit.toMillis(amount));
        return c;
    }

    /**
     * カレンダーの年、月、日部分をクリア.
     *
     *
     * @param cal カレンダー
     * @return 入力されたカレンダー
     */
    @Nonnull
    static public Calendar clearDatePart(@NonNull Calendar cal) {
        cal.set(YEAR, 1970);
        cal.set(MONTH, JANUARY);
        cal.set(DAY_OF_MONTH, 1);
        return cal;
    }

    /**
     * カレンダーの時間部分をクリア.
     *
     * @param cal カレンダー
     * @return 入力されたカレンダー
     */
    @Nonnull
    static public Calendar clearTimePart(@NonNull Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
        return cal;
    }

    /**
     * weeks 内部にある曜日と一致する基準日より後で、ベース直近の日付を求める.
     *
     * @param cal ベースとなる日付
     * @param weeks ソート済み週リスト.
     * @param currentTime 基準日
     * @return 週リスト内の週に一致する直近の日付
     */
    public static Calendar computeNextWeekday(Calendar cal, List<Integer> weeks, Calendar currentTime) {
        Calendar work = (Calendar) cal.clone();
        do {
            if (work.compareTo(currentTime) <= 0) {
                // 同時刻なら次回
                work.add(Calendar.DAY_OF_MONTH, 1);
            }
            for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++, work.add(Calendar.DAY_OF_MONTH, 1)) {
                int wod = work.get(Calendar.DAY_OF_WEEK);
                if (0 <= Collections.binarySearch(weeks, wod)) {
                    // 値がある.
                    break;
                }
            }
        }
        while (work.compareTo(currentTime) <= 0);
        return work;
    }

    /**
     * フィールドのコピー.
     *
     * @param dest コピー対象.
     * @param src コピー元.
     * @param copyFields コピーするフィールド.
     * @return dest.
     */
    @Nonnull
    static public Calendar copyFields(@NonNull Calendar dest, @NonNull Calendar src, int... copyFields) throws IllegalArgumentException {
        for (int copyField : copyFields) {
            dest.set(copyField, src.get(copyField));
        }
        return dest;
    }

    /**
     * 年、月、日から、日付オブジェクトを作成.
     *
     * @param year 年
     * @param month 月（1～12）
     * @param day 日
     * @return 日付オブジェクト
     * @throws IllegalArgumentException 年、月、日の範囲が不正.
     */
    @Nonnull
    public static Calendar createCalendar(int year, int month, int day) throws IllegalArgumentException {
        return createCalendar(year, month, day, 0, 0, 0, 0);
    }

    /**
     * 年、月、日、時、分、秒から日付オブジェクトを作成.<br> {@link Calendar#setLenient(boolean) } に
     * false を設定し、<br>
     * 日付が厳密に設定されているかどうかを確認する.<br>
     *
     * @param year 年
     * @param month 月（1～12）
     * @param day 日
     * @param hourOfDay 時（24時表記）
     * @param minute 分
     * @param second 秒
     * @return 日付オブジェクト
     * @throws IllegalArgumentException 年、月、日、時、分、秒の範囲が不正.
     */
    @Nonnull
    public static Calendar createCalendar(int year, int month, int day, int hourOfDay, int minute, int second) throws IllegalArgumentException {
        return createCalendar(year, month, day, hourOfDay, minute, second, 0);
    }

    /**
     * 年、月、日、時、分、秒から日付オブジェクトを作成.<br> {@link Calendar#setLenient(boolean) } に
     * false を設定し、<br>
     * 日付が厳密に設定されているかどうかを確認する.<br>
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @param hourOfDay 時（24時表記）
     * @param minute 分
     * @param second 秒
     * @param millisecond ミリ秒
     * @return 日付オブジェクト
     * @throws IllegalArgumentException 年、月、日、時、分、秒、ミリ秒の範囲が不正.
     */
    @Nonnull
    public static Calendar createCalendar(int year, int month, int day, int hourOfDay, int minute, int second, int millisecond) throws IllegalArgumentException {

        between(month, 1, 12, "month");
        Calendar c = Calendar.getInstance();
        c.setLenient(false);

        between(hourOfDay, 0, 23, "hourOfDay");
        between(minute, 0, 59, "minute");
        between(second, 0, 59, "second");
        between(millisecond, 0, 999, "millisecond");
        c.set(HOUR_OF_DAY, hourOfDay);
        c.set(MINUTE, minute);
        c.set(SECOND, second);
        c.set(MILLISECOND, millisecond);
        c.set(year, month - 1, 1);

        int max = c.getActualMaximum(DAY_OF_MONTH);
        between(day, 1, max, "day");
        c.set(DAY_OF_MONTH, day);
        c.getTime();
        return c;
    }

    /**
     * 年、月、日から、日付オブジェクトを作成.
     *
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 日付オブジェクト
     * @throws IllegalArgumentException 年、月、日の範囲が不正.
     */
    @Nonnull
    public static Date createDate(int year, int month, int day) throws IllegalArgumentException {
        return createCalendar(year, month, day).getTime();
    }

    /**
     *
     * @param from
     * @param to
     * @return
     */
    @Nonnull
    public static Date createRandomDate(Date from, Date to) {
        final long n = to.getTime() - from.getTime();
        final Random r = new Random();
        long bits;
        long val;
        do {
            bits = r.nextLong();
            if (bits < 0) {
                bits -= Long.MIN_VALUE;
            }
            val = bits % n;
        }
        while (bits - val + (n - 1) < 0);
        return new Date(from.getTime() + val);
    }

    /**
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null) {
            return date.toString();
        }
        SimpleDateFormat fmt = ServiceProviders.resolveService(SimpleDateFormat.class, Tag.of(format));
        return fmt.format(date);
    }

    /**
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Calendar date, String format) {
        if (date == null) {
            return null;
        }
        if (format == null) {
            return date.toString();
        }
        SimpleDateFormat fmt = ServiceProviders.resolveService(SimpleDateFormat.class, Tag.of(format));
        return fmt.format(date.getTime());
    }

    /**
     * @return 日付取得器を取得.
     */
    @Nonnull
    public static CurrentDateProvider getCurrentDateProvider() {
        CurrentDateProvider dp = get(CurrentDateProvider.class);
        if (dp == null) {
            dp = new DefaultCurrentDateProvider();
        }
        return dp;
    }

    /**
     * 日付部分のみを取り出す.
     *
     * @param date 日付型
     * @return 日付部分のみを取り出した日付型
     */
    @Nonnull
    public static Date getDatePart(@NonNull Date date) {
        return getDatePart(toCalendar(date, null)).getTime();
    }

    /**
     * 日付部分のみを取り出す.
     *
     * @param cal 日付型
     * @return 日付部分のみを取り出した日付型
     */
    @Nonnull
    public static Calendar getDatePart(@NonNull Calendar cal) {
        Calendar newCal = (Calendar) cal.clone();
        newCal.clear();
        return copyFields(newCal, cal, DATE_PART_FIELDS);
    }

    /**
     * 時刻部分のみを取り出す.
     *
     * @param date 日付型
     * @return 時刻部分のみを取り出した日付型
     */
    @Nonnull
    public static Date getTimePart(@NonNull Date date) {
        return getTimePart(toCalendar(date, null)).getTime();
    }

    /**
     * 時刻部分のみを取り出す.
     *
     * @param cal 日付型
     * @return 時刻部分のみを取り出した日付型
     */
    @Nonnull
    public static Calendar getTimePart(@NonNull Calendar cal) {
        Calendar newCal = (Calendar) cal.clone();
        newCal.clear();
        return copyFields(newCal, cal, TIME_PART_FIELDS);
    }

    /**
     * 指定されたカレンダーオブジェクトの日付が1970-1-1かどうか調査する.
     *
     * @param cal
     * @return
     */
    public static boolean isEpochDatePart(@NonNull Calendar cal) {
        return isSameDay(cal, LOCAL_EPOC_CALENDAR);
    }

    /**
     * 指定されたカレンダーオブジェクトの時刻が00:00:00.000かどうか調査する. 時差は考慮しない.
     *
     * @param cal
     * @return
     */
    public static boolean isEpochTimePart(@NonNull Calendar cal) {
        return cal.get(Calendar.HOUR_OF_DAY) == 0
                && cal.get(Calendar.MINUTE) == 0
                && cal.get(Calendar.SECOND) == 0
                && cal.get(Calendar.MILLISECOND) == 0;
    }

    /**
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public static boolean isSameDay(Calendar lhs, Calendar rhs) {
        if (lhs == rhs) {
            return true;
        }
        else if (rhs == null || lhs == null) {
            return false;
        }
        else {
            return lhs.get(ERA) == rhs.get(ERA)
                    && lhs.get(YEAR) == rhs.get(YEAR)
                    && lhs.get(DAY_OF_YEAR) == rhs.get(DAY_OF_YEAR);
        }
    }

    /**
     *
     * @return 現在のカレンダーを取得.
     */
    @Nonnull
    public static Calendar now() {
        return getCurrentDateProvider().getNow();
    }

    /**
     * 現在時刻をナノ秒まで含めて取得.
     *
     * @return 現在のタイムスタンプ.
     */
    public static Timestamp nowTimestamp() {
        CurrentDateProvider p = getCurrentDateProvider();
        Calendar cal = p.getNow();
        long timeInNanos = p.getNanoTime();
        Timestamp ts = new Timestamp(cal.getTime().getTime());
        ts.setNanos((int) (timeInNanos % 1000000000));
        return ts;
    }

    /**
     * 日付テキストをカレンダーに変換する. <br> {@link DateTimePattern}にあるフォーマットを使用する.<br>
     * {@link DateTimePattern}に一致しない場合、<br>
     * {@link SimpleDateFormat#getInstance() }を使用して変換を試みる.
     *
     * @param dateText 日付テキスト
     * @return カレンダー.
     * @throws ParseException dateText が空、または解析に失敗した場合.
     */
    @Nonnull
    public static Calendar parseCalendar(String dateText) throws ParseException {
        if (isEmpty(dateText)) {
            throw new ParseException("dateText is must be not empty.", 0);
        }
        for (DateTimePattern p : DateTimePattern.values()) {
            Calendar result = Calendar.getInstance();
            if (p.toCalendar(dateText, result)) {
                result.setLenient(false);
                result.getTime();
                return result;
            }
        }

        Date date = SimpleDateFormat.getInstance().parse(dateText);
        return toCalendar(date, null);
    }

    private static <T> T get(Class<T> clazz) {
        return getThreadContainer().resolveService(clazz);
    }

    /**
     *
     * @return 短縮年変換器を追加.
     */
    @Nonnull
    static ShortYearConverter getShortYearConverter() {
        ShortYearConverter conv = get(ShortYearConverter.class);
        if (conv == null) {
            conv = new DefaultShortYearConverter();
        }
        return conv;
    }

    private ClassicDateUtils() {
    }
}
