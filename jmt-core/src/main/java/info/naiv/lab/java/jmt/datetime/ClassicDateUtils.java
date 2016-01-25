package info.naiv.lab.java.jmt.datetime;

import static info.naiv.lab.java.jmt.Arguments.*;
import info.naiv.lab.java.jmt.Constants;
import static info.naiv.lab.java.jmt.Misc.*;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.*;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ClassicDateUtils {

    public static final int[] DATE_PART_FIELDS = {YEAR, MONTH, DAY_OF_MONTH};

    public static final Calendar EPOC_CALENDAR;
    public static final Date EPOC_DATE;

    public static final int[] TIME_PART_FIELDS = {HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND};

    static {
        EPOC_DATE = new Date(0);
        EPOC_CALENDAR = toCalendar(EPOC_DATE, null);
    }

    /**
     * 日付を加算する. 既存のオブジェクトは変更しない.
     *
     * @param date 日付
     * @param amount 加算する値
     * @param timeUnit 単位
     * @return 結果
     */
    @ReturnNonNull
    public static Date add(Date date, long amount, TimeUnit timeUnit) {
        nonNull(date, "date");
        nonNull(timeUnit, "timeUnit");
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
    @ReturnNonNull
    public static Calendar add(Calendar cal, long amount, TimeUnit timeUnit) {
        nonNull(cal, "cal");
        nonNull(timeUnit, "timeUnit");
        Calendar c = (Calendar) cal.clone();
        c.setTimeInMillis(cal.getTimeInMillis() + timeUnit.toMillis(amount));
        return c;
    }

    /**
     * 営業日計算.
     * 指定された日付が休日の場合、{@link #shiftIfHoliday(java.util.Calendar, info.naiv.java.jmt.datetime.WorkingDaySettings) }
     * を呼び出し、営業日にしてから処理を行う
     *
     * @param cal 日付
     * @param days 加算する営業日.
     * @param settings 設定.
     * @return 営業日分だけ加算した値.
     */
    @ReturnNonNull
    public static Calendar addWorkingDays(Calendar cal, int days, WorkingDaySettings settings) {
        nonNull(cal, "cal");
        nonNull(settings, "settings");

        int mod = days < 0 ? -1 : 1;
        int absDays = mod * days;
        Calendar result = shiftIfHoliday(cal, settings);
        for (int i = 0; i < absDays; i++) {
            result.add(DAY_OF_MONTH, mod);
            while (settings.isHoliday(result)) {
                result.add(DAY_OF_MONTH, mod);
            }
        }
        return result;
    }

    /**
     * カレンダーの年、月、日部分をクリア.
     *
     *
     * @param cal カレンダー
     * @return 入力されたカレンダー
     */
    @ReturnNonNull
    static public Calendar clearDatePart(Calendar cal) {
        nonNull(cal, "cal");
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
    @ReturnNonNull
    static public Calendar clearTimePart(Calendar cal) {
        nonNull(cal, "cal");
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
        return cal;
    }

    /**
     * 指定された週の最初の営業日を取得する.週の開始日が休日の場合、翌平日を戻す.
     *
     * @param cal カレンダー
     * @param settings 営業日情報.
     * @return 週の最初の営業日.
     */
    @ReturnNonNull
    static public Calendar computeFirstBizDayOfWeek(Calendar cal, WorkingDaySettings settings) {
        nonNull(cal, "cal");
        nonNull(settings, "holidaySettings");
        final Calendar base = (Calendar) cal.clone();
        final int fdow = settings.getWeekSettings().getFirstDayOfWeek();
        final int dow = base.get(DAY_OF_WEEK);
        final int mod = (dow < fdow) ? Constants.SEVEN_DAYS : 0;
        base.setFirstDayOfWeek(fdow);
        base.add(DAY_OF_MONTH, fdow - dow - mod);
        while (settings.isHoliday(base)) {
            base.add(DAY_OF_MONTH, 1);
        }
        return base;
    }

    /**
     * フィールドのコピー.
     *
     * @param dest コピー対象.
     * @param src コピー元.
     * @param copyFields コピーするフィールド.
     * @return dest.
     */
    @ReturnNonNull
    static public Calendar copyFields(Calendar dest, Calendar src, int... copyFields) throws IllegalArgumentException {
        nonNull(dest, "dest");
        nonNull(src, "src");
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
    @ReturnNonNull
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
    @ReturnNonNull
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
    @ReturnNonNull
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
    @ReturnNonNull
    public static Date createDate(int year, int month, int day) throws IllegalArgumentException {
        return createCalendar(year, month, day).getTime();
    }

    @ReturnNonNull
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
     * @return 日付取得器を取得.
     */
    @ReturnNonNull
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
    @ReturnNonNull
    public static Date getDatePart(Date date) {
        nonNull(date, "date");
        return getDatePart(toCalendar(date, null)).getTime();
    }

    /**
     * 日付部分のみを取り出す.
     *
     * @param cal 日付型
     * @return 日付部分のみを取り出した日付型
     */
    @ReturnNonNull
    public static Calendar getDatePart(Calendar cal) {
        nonNull(cal, "cal");
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
    @ReturnNonNull
    public static Date getTimePart(Date date) {
        nonNull(date, "date");
        return getTimePart(toCalendar(date, null)).getTime();
    }

    /**
     * 時刻部分のみを取り出す.
     *
     * @param cal 日付型
     * @return 時刻部分のみを取り出した日付型
     */
    @ReturnNonNull
    public static Calendar getTimePart(Calendar cal) {
        nonNull(cal, "cal");
        Calendar newCal = (Calendar) cal.clone();
        newCal.clear();
        return copyFields(newCal, cal, TIME_PART_FIELDS);
    }

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
    @ReturnNonNull
    public static Calendar now() {
        return getCurrentDateProvider().getNow();
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
    @ReturnNonNull
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

    /**
     * 日付が休日なら、営業日までシフトした日付を戻し、<br>
     * そうでなければカレンダーを戻す.
     *
     * @param cal
     * @param settings
     * @return
     */
    @ReturnNonNull
    public static Calendar shiftIfHoliday(Calendar cal, WorkingDaySettings settings) {
        nonNull(cal, "cal");
        nonNull(settings, "settings");

        Calendar result = (Calendar) cal.clone();
        int shift = settings.isShiftForward() ? 1 : -1;
        while (settings.isHoliday(result)) {
            result.add(DAY_OF_MONTH, shift);
        }
        return result;
    }

    private static <T> T get(Class<T> clazz) {
        return getThreadContainer().resolveService(clazz);
    }

    /**
     *
     * @return 短縮年変換器を追加.
     */
    @ReturnNonNull
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
