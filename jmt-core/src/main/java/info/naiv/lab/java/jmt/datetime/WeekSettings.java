package info.naiv.lab.java.jmt.datetime;

import static info.naiv.lab.java.jmt.Arguments.between;
import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Misc.*;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.io.Serializable;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author enlo
 */
@EqualsAndHashCode
@ToString
public final class WeekSettings implements Serializable {

    /**
     * 既定の設定. 日曜始まり、日曜法定休、土曜休
     */
    public static final WeekSettings DEFAULT = new WeekSettings(SUNDAY, SUNDAY, new int[]{SATURDAY});
    private static final long serialVersionUID = -7840943486691329944L;

    /**
     * 週の開始曜日
     */
    private final int firstDayOfWeek;

    /**
     * 法定休日
     */
    private final int officialHoliday;

    /**
     * その他休日
     */
    private final int[] unofficialHolidays;

    /**
     * コンストラクター
     *
     * @param firstDayOfWeek 週の開始曜日
     * @param officialHoliday 法定休日
     * @param unofficialHolidays その他休日
     */
    public WeekSettings(int firstDayOfWeek, int officialHoliday, int[] unofficialHolidays) {
        check(firstDayOfWeek, "firstDayOfWeek");
        check(officialHoliday, "officialHoliday");
        check(unofficialHolidays, "unofficialHolidays");
        this.firstDayOfWeek = firstDayOfWeek;
        this.officialHoliday = officialHoliday;
        this.unofficialHolidays = unofficialHolidays;
    }

    /**
     * コンストラクター
     *
     * @param firstDayOfWeek 週の開始曜日
     * @param officialHoliday 法定休日
     */
    public WeekSettings(int firstDayOfWeek, int officialHoliday) {
        this(firstDayOfWeek, officialHoliday, new int[]{});
    }

    /**
     *
     * @return 週の開始曜日
     */
    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    /**
     * @return 法定休日
     */
    public int getOfficialHoliday() {
        return officialHoliday;
    }

    /**
     * @return その他休日
     */
    @ReturnNonNull
    public int[] getUnofficialHolidays() {
        return unofficialHolidays.clone();
    }

    /**
     * 休日かどうかチェック.
     *
     * @param weekday 曜日
     * @return 休日なら true.
     */
    public boolean isHoliday(int weekday) {
        boolean result = weekday == officialHoliday;
        if (result) {
            return true;
        }
        for (int i = 0; i < unofficialHolidays.length; i++) {
            int unofficialHoliday = unofficialHolidays[i];
            if (unofficialHoliday == weekday) {
                return true;
            }
        }
        return false;
    }

    /**
     * 週の開始曜日を変更して新しく WeekSettings を作成. 法定休日と、法定外休日は今の設定を継承する.
     *
     * @param fdow 新しい開始曜日
     * @return 新しく作成した WeekSettings.
     */
    @ReturnNonNull
    public WeekSettings newFirstDayOfWeek(int fdow) {
        return new WeekSettings(fdow, this.officialHoliday, this.unofficialHolidays);
    }

    private void check(int weekday, String vername) {
        between(weekday, SUNDAY, SATURDAY, vername);
    }

    private void check(int[] weekdays, String vername) {
        nonNull(weekdays, vername);
        for (int weekday : weekdays) {
            between(weekday, SUNDAY, SATURDAY, vername);
        }
    }

}
