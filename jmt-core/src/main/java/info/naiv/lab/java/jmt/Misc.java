package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.iterator.FlatIterableIterator;
import info.naiv.lab.java.jmt.iterator.MappingIterator;
import static info.naiv.lab.java.jmt.Arguments.nonEmpty;
import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;
import static info.naiv.lab.java.jmt.Constants.ZWNBSP;
import info.naiv.lab.java.jmt.datetime.ClassicDateUtils;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.parseCalendar;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.mark.Nop;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import static java.lang.Character.isSpaceChar;
import static java.lang.Character.isWhitespace;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Misc {

    /**
     * リストにオブジェクトが存在しない場合追加する.
     *
     * @param <T> 要素の型
     * @param list リスト
     * @param obj 追加したいオブジェクト
     * @return 追加した場合 true.
     */
    public static <T> boolean addIfNotFound(Collection<T> list, T obj) {
        if (list == null) {
            return false;
        }
        for (T item : list) {
            if (item.equals(obj)) {
                return false;
            }
        }
        return list.add(obj);
    }

    public static <T> Iterator<T> advance(Iterator<T> iter, int n) {
        for (int i = 0; i < n && iter.hasNext(); i++) {
            iter.next();
        }
        return iter;
    }

    /**
     * 値が範囲内に収まっているかどうかチェック. value, from, to のいずれかが null の場合は false.
     *
     * @param <T> 値の型.
     * @param value 検査する値.
     * @param from 開始.
     * @param to 終了.
     * @return from &lt= value &lt= to
     */
    public static <T extends Comparable<T>> boolean between(T value, T from, T to) {
        if (value == null || from == null || to == null) {
            return false;
        }
        else {
            return from.compareTo(value) <= 0 && value.compareTo(to) <= 0;
        }
    }

    /**
     * コレクションから受け入れ可能なものを取得
     *
     * @param <T> 要素の型
     * @param items コレクションから等価であるものを取得
     * @param predicate 検索
     * @return 等価な項目があれば true.
     */
    public static <T> boolean contains(Iterable<? extends T> items, Predicate1<? super T> predicate) {
        nonNull(predicate, "predicate");
        if (items == null) {
            return false;
        }
        for (T item : items) {
            if (predicate.test(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * コレクションから等価であるものを取得
     *
     * @param <T> 要素の型
     * @param items コレクションから等価であるものを取得
     * @param valueToFind 検索する値
     * @return 等価な項目があれば true.
     */
    public static <T extends Comparable<T>>
            boolean containsCompareEquals(Iterable<? extends T> items, T valueToFind) {
        if (items == null) {
            return false;
        }
        for (T item : items) {
            if (item.compareTo(valueToFind) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 同じ型同士で比較.
     *
     * @param <T>
     * @param lhs
     * @param rhs
     * @return
     */
    public static <T> boolean equals(T lhs, T rhs) {
        return Objects.equals(lhs, rhs);
    }

    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable != null) {
            for (T i : iterable) {
                return i;
            }
        }
        return null;
    }

    /**
     * 値からキーを検索する.
     *
     * @param <TKey> キーの型
     * @param <TValue> 値の型
     * @param map マップ
     * @param value 値
     * @return キーセット
     */
    @ReturnNonNull
    public static <TKey, TValue> Set<TKey> getKeySetByValue(Map<TKey, TValue> map, TValue value) {
        nonNull(map, "map");
        Set<TKey> keys = new HashSet<>();
        for (Entry<TKey, TValue> e : map.entrySet()) {
            if (Objects.equals(e.getValue(), value)) {
                keys.add(e.getKey());
            }
        }
        return keys;
    }

    /**
     * 中身が空白かどうかチェック.
     *
     * @param object チェックする文字列.
     * @return 空か空白のみなら true.
     * @see Character#isWhitespace(char)
     */
    public static boolean isBlank(String object) {
        if (isEmpty(object)) {
            return true;
        }
        else {
            int l = object.length();
            for (int i = 0; i < l; i++) {
                char ch = object.charAt(i);
                if (!isWhitespace(ch) && !isSpaceChar(ch) && ch != 0xFEFF) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 中身が空かどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空ならば true.
     */
    public static boolean isEmpty(String object) {
        return object == null || object.isEmpty();
    }

    /**
     * 中身が空かどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空ならば true.
     */
    public static boolean isEmpty(Iterable<?> object) {
        if (object == null) {
            return true;
        }
        else {
            Iterator<?> it = object.iterator();
            return it == null || !it.hasNext();
        }
    }

    /**
     * 中身が空かどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空ならば true.
     */
    public static boolean isEmpty(Collection<?> object) {
        return object == null || object.isEmpty();
    }

    /**
     * 中身が空かどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空ならば true.
     */
    public static boolean isEmpty(Map<?, ?> object) {
        return object == null || object.isEmpty();
    }

    /**
     * 中身が空かどうかチェック.
     *
     * @param <T> 配列の型
     * @param object チェックするオブジェクト
     * @return 空ならば true.
     */
    public static <T> boolean isEmpty(T[] object) {
        return object == null || object.length == 0;
    }

    /**
     * クラスがロードできるかどうか.
     *
     * @param className
     * @return
     */
    public static boolean isLoadable(String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (ClassNotFoundException ex) {
            logger.trace("{} is not found. {}", className, ex.getMessage());
            return false;
        }
    }

    /**
     * クラスがロードできるかどうか.
     *
     * @param className
     * @param loader
     * @return
     */
    public static boolean isLoadable(String className, ClassLoader loader) {
        try {
            Class.forName(className, true, loader);
            return true;
        }
        catch (ClassNotFoundException ex) {
            logger.trace("{} is not found. {}", className, ex.getMessage());
            return false;
        }
    }

    /**
     * 中身が空白でないことをチェック.
     *
     * @param object チェックする文字列
     * @return 空でも空白でなければ true.
     */
    public static boolean isNotBlank(String object) {
        return !isBlank(object);
    }

    /**
     * 中身があるかどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空でなければ true.
     */
    public static boolean isNotEmpty(String object) {
        return !isEmpty(object);
    }

    /**
     * 中身があるかどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空でなければ true.
     */
    public static boolean isNotEmpty(Iterable<?> object) {
        return !isEmpty(object);
    }

    /**
     * 中身があるかどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空でなければ true.
     */
    public static boolean isNotEmpty(Collection<?> object) {
        return !isEmpty(object);
    }

    /**
     * 中身があるかどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空でなければ true.
     */
    public static boolean isNotEmpty(Map<?, ?> object) {
        return !isEmpty(object);
    }

    /**
     * 中身があるかどうかチェック.
     *
     * @param <T> 配列の型
     * @param object チェックするオブジェクト
     * @return 空でなければ true.
     */
    public static <T> boolean isNotEmpty(T[] object) {
        return !isEmpty(object);
    }

    /**
     * 文字列連結.
     *
     * @param items 連結する項目.
     * @param delim 区切り文字
     * @return 連結された文字列.
     */
    @ReturnNonNull
    public static String join(Iterable<?> items, String delim) {
        return (StringJoiner.valueOf(delim)).join(items).toString();
    }

    /**
     * 文字列連結.
     *
     * @param items 連結する項目
     * @return 連結された文字列.
     */
    @ReturnNonNull
    public static String join(Object... items) {
        return arrayToString(items);
    }

    /**
     * コレクションを別のコレクションに変換.
     *
     * @param <Dest> 変換先のコレクションの型.
     * @param <R> 変換先の型
     * @param <T> 変換元の型
     * @param dest 変換先のコレクション
     * @param source 元のコレクション
     * @param mapper 変換器
     * @return 変換先のコレクション
     * @throws IllegalArgumentException dest または mapper が null
     */
    @ReturnNonNull
    public static <Dest extends Collection<R>, R, T>
            Dest map(Dest dest, Collection<T> source, Function1<? super T, R> mapper)
            throws IllegalArgumentException {
        nonNull(dest, "dest");
        nonNull(mapper, "mapper");
        if (source != null) {
            for (T item : source) {
                dest.add(mapper.apply(item));
            }
        }
        return dest;
    }

    @ReturnNonNull
    public static <T, U>
            Iterable<U> map(final Iterable<T> iter, final Function1<? super T, ? extends U> mapper) {
        return new Iterable<U>() {
            @Override
            public Iterator<U> iterator() {
                return new MappingIterator<>(iter.iterator(), mapper);
            }
        };
    }

    /**
     * マップの値を変換し、別のマップに登録.
     *
     * @param <Dest> 変換先のマップの型.
     * @param <K> キーの型
     * @param <R> 変換先の型
     * @param <T> 変換元の型
     * @param dest 変換先のマップ
     * @param source 元のマップ
     * @param mapper 変換器
     * @return 変換先のマップ
     */
    @ReturnNonNull
    public static <Dest extends Map<K, R>, K, R, T> Dest map(Dest dest, Map<K, T> source, Function1<T, R> mapper) {
        nonNull(dest, "dest");
        if (source != null) {
            for (Entry<K, T> e : source.entrySet()) {
                K key = e.getKey();
                dest.put(key, mapper.apply(e.getValue()));
            }
        }
        return dest;
    }

    /**
     * 集合を別の型の集合に変換.
     *
     * @param <R> 変換先の型
     * @param <T> 変換元の型
     * @param source 元の集合
     * @param mapper 変換器
     * @return dest.
     */
    @ReturnNonNull
    public static <R, T> Set<R> map(Set<T> source, Function1<T, R> mapper) {
        final Set<R> result = new HashSet<>(source.size());
        return map(result, source, mapper);
    }

    /**
     * リストを別の型のリストに変換.
     *
     * @param <R> 変換先の型
     * @param <T> 変換元の型
     * @param source 元のリスト
     * @param mapper 変換器
     * @return dest.
     */
    @ReturnNonNull
    public static <R, T> List<R> map(List<T> source, Function1<T, R> mapper) {
        final List<R> result = new ArrayList<>(source.size());
        return map(result, source, mapper);
    }

    @ReturnNonNull
    public static <T> Iterable<T> flat(final Iterable<? extends Iterable<T>> items) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new FlatIterableIterator<>(items);
            }
        };
    }

    /**
     * 2つの値を比較し、小さい方を戻す。
     *
     * @param <T>
     * @param x
     * @param y
     * @return x または y の小さい方. どちらかが null なら null でない方を戻す.
     */
    public static <T extends Comparable<T>> T min(T x, T y) {
        return new ComparableComparator<T>().min(x, y);
    }

    /**
     * No Operation.
     *
     * @param <T>
     * @param x
     */
    @Nop
    public static <T> void nop(T x) {

    }

    /**
     * 先頭の ZWNBSP (BOM) を除外.
     *
     * @param string 文字列
     * @return 先頭から ZWNBSP を除外した文字列.
     */
    public static String removeZwnbsp(String string) {
        if (isEmpty(string)) {
            return string;
        }
        else if (string.startsWith(ZWNBSP)) {
            return string.substring(ZWNBSP.length());
        }
        else {
            return string;
        }
    }

    /**
     * 繰り返し値を取得する.
     *
     * @param <T> 項目の型
     * @param repeatMax 繰り返す回数
     * @param value 値
     * @return Iterable
     */
    @ReturnNonNull
    public static <T> Iterable<T> repeat(int repeatMax, T value) {
        return new Repeater<>(repeatMax, value);
    }

    /**
     * Number を BigDecimal に変換
     *
     * @param source ソース
     * @param defaultValue 既定値.
     * @return 変換先. source が null なら defaultValue.
     */
    public static BigDecimal toBigDecimal(Number source, BigDecimal defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        else if (source instanceof BigDecimal) {
            return (BigDecimal) source;
        }
        else if (source instanceof Integer || source instanceof Long || source instanceof Short || source instanceof Byte) {
            return BigDecimal.valueOf(source.longValue());
        }
        else if (source instanceof BigInteger) {
            return new BigDecimal((BigInteger) source);
        }
        else {
            return new BigDecimal(source.toString());
        }
    }

    /**
     * 文字列から BigDecimal を作成.
     *
     * @param source 文字列
     * @param defaultValue エラー時の既定値
     * @return 変換結果. source が null なら defaultValue.
     */
    public static BigDecimal toBigDecimal(String source, BigDecimal defaultValue) {
        if (isNotEmpty(source)) {
            try {
                return new BigDecimal(source);
            }
            catch (NumberFormatException e) {
                logger.warn("to bigdecimal error. source:{}, throws:{}", source, e);
            }
        }
        return defaultValue;
    }

    @ReturnNonNull
    public static byte[] toByteArray(String text, String charset) {
        if (text == null) {
            return new byte[]{};
        }
        Charset cs = CharsetMap.get(charset);
        return text.getBytes(cs);
    }

    /**
     * Dateをカレンダーに変換.
     *
     * @param date 日付
     * @param defaultValue 既定値
     * @return 値
     */
    public static Calendar toCalendar(Date date, Calendar defaultValue) {
        if (date == null) {
            return defaultValue;
        }
        else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
    }

    /**
     * 文字列をカレンダーに変換.<br> {@link ClassicDateUtils#parseCalendar(String)} を呼び出す.
     *
     * @param dateText 日付テキスト
     * @param defaultValue 既定値
     * @return 値
     */
    public static Calendar toCalendar(String dateText, Calendar defaultValue) {
        if (isNotEmpty(dateText)) {
            try {
                return parseCalendar(dateText);
            }
            catch (ParseException ex) {
                logger.warn("parse error.", ex);
            }
        }
        return defaultValue;
    }

    /**
     * 文字列を日付に変換. {@link ClassicDateUtils#parseCalendar(String)} を呼び出す.
     *
     * @param dateText 日付テキスト
     * @param defaultValue 値
     * @return 値
     */
    public static Date toDate(String dateText, Date defaultValue) {
        if (isNotEmpty(dateText)) {
            try {
                Calendar c = parseCalendar(dateText);
                return c.getTime();
            }
            catch (ParseException ex) {
                logger.warn("parse error.", ex);
            }
        }
        return defaultValue;
    }

    public static Double toDoubleNum(Number source, Double defaultValue) {
        if (source != null) {
            return source.doubleValue();
        }
        else {
            return defaultValue;
        }
    }

    public static Float toFloatNum(Number source, Float defaultValue) {
        if (source != null) {
            return source.floatValue();
        }
        else {
            return defaultValue;
        }
    }

    /**
     * Number を int に変換.
     *
     * @param source ソース
     * @param defaultValue 既定値
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static int toInt(Number source, int defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        return source.intValue();
    }

    /**
     * 文字列を int に変更.
     *
     * @param source 数値文字列.
     * @param defaultValue 既定値.
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static int toInt(String source, int defaultValue) {
        Integer x = toIntNum(source, null);
        return x != null ? x : defaultValue;
    }

    /**
     * Number を Integer に変換.
     *
     * @param source ソース
     * @param defaultValue 既定値
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static Integer toIntNum(Number source, Integer defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        return source.intValue();
    }

    /**
     * 文字列をIntegerに変更.
     *
     * @param source 数値文字列.
     * @param defaultValue 既定値.
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static Integer toIntNum(String source, Integer defaultValue) {
        Number num = toNumber(source, null);
        if (num != null) {
            return num.intValue();
        }
        else {
            return defaultValue;
        }
    }

    /**
     * Iterable を List に変換.
     *
     * @param <T>
     * @param iter
     * @return
     */
    @ReturnNonNull
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Iterable<T> iter) {
        if (iter instanceof Collection) {
            return new ArrayList<>((Collection) iter);
        }
        else {
            List<T> list = new ArrayList<>();
            if (iter != null) {
                for (T obj : iter) {
                    list.add(obj);
                }
            }
            return list;
        }
    }

    /**
     * Number を Long に変換.
     *
     * @param source ソース
     * @param defaultValue 既定値
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static long toLong(Number source, long defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        return source.longValue();
    }

    /**
     * 文字列をLongに変更.
     *
     * @param source 数値文字列.
     * @param defaultValue 既定値.
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static long toLong(String source, long defaultValue) {
        return toLong(toNumber(source, null), defaultValue);
    }

    /**
     * Number を Long に変換.
     *
     * @param source ソース
     * @param defaultValue 既定値
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static Long toLongNum(Number source, Long defaultValue) {
        if (source == null) {
            return defaultValue;
        }
        return source.longValue();
    }

    /**
     * 文字列をLongに変更.
     *
     * @param source 数値文字列.
     * @param defaultValue 既定値.
     * @return 変換結果. 変換できない場合はdefaultValue.
     */
    public static Long toLongNum(String source, Long defaultValue) {
        Number num = toNumber(source, null);
        if (num != null) {
            return num.longValue();
        }
        else {
            return defaultValue;
        }
    }

    /**
     * 文字列を数値に変換する. <br>
     * サービスプロバイダーから、NumberFormat を取得し、処理を行う. <br>
     * 戻される値は Long か Double のいずれか.
     *
     * @param source 文字列
     * @param defaultValue 解析できない場合の既定値.
     * @return 結果.
     */
    public static Number toNumber(String source, Number defaultValue) {
        return toNumber(source, defaultValue, null);
    }

    /**
     * 文字列を数値に変換する. <br>
     * サービスプロバイダーから、NumberFormat を取得する. <br>
     * ヒントとなる型を与え、処理を変更可能. ヒントとなる型と結果は以下の通り.
     * <dl>
     * <dt>{@link BigDecimal#class}</dt>
     * <dd>BigDecimal として処理.<dd>
     * <dt>{@link Long#class}</dt>
     * <dd>Long として処理.<dd>
     * <dt>上記以外</dt>
     * <dd>文字列に応じて、Long または Double として処理.<dd>
     * </dl>
     *
     * @param source 文字列
     * @param defaultValue 解析できない場合の既定値.
     * @param hint 型のヒント.
     * @return 結果.
     */
    public static Number toNumber(String source, Number defaultValue, Class<? extends Number> hint) {
        if (isNotEmpty(source)) {
            try {
                ServiceProvider sp = getThreadContainer();
                Tag tag = hint != null ? Tag.of(hint) : Tag.of(Number.class);
                NumberFormat format = sp.resolveService(NumberFormat.class, tag);
                return format.parse(source);
            }
            catch (ParseException ex) {
                logger.warn("toNumber error.", ex);
            }
        }
        return defaultValue;
    }

    /**
     * バイト列から文字列を作成する. {@link Charset#forName(java.lang.String)}
     * は、1つのキャッシュだけを持つため、 頻繁に文字コードを変えるような場合はこちらを利用する.
     *
     * @param bytes
     * @param charset
     * @return
     */
    @ReturnNonNull
    public static String toString(byte[] bytes, String charset) {
        Charset cs = CharsetMap.get(charset);
        return new String(bytes, cs);
    }

    /**
     * バイト列から文字列を作成する. {@link Charset#forName(java.lang.String)}
     * は、1つのキャッシュだけを持つため、 頻繁に文字コードを変えるような場合はこちらを利用する.
     *
     * @param bytes
     * @param offset
     * @param length
     * @param charset
     * @return
     */
    @ReturnNonNull
    public static String toString(byte[] bytes, int offset, int length, String charset) {
        Charset cs = CharsetMap.get(charset);
        return new String(bytes, offset, length, cs);
    }

    /**
     * オブジェクトを formatter を通して文字列に変換.
     *
     * @param value 値
     * @param format フォーマット
     * @param defaultValue 変換に失敗した場合や null の場合に出力する値.
     * @return 結果
     */
    public static String toString(Object value, Format format, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (format != null) {
            try {
                return format.format(value);
            }
            catch (Exception e) {
                logger.warn("format failed. value:{} format:{} throws:{}", value, format, e);
                return defaultValue;
            }
        }
        else {
            return value.toString();
        }
    }

    public static URL toURL(String url) {
        nonEmpty(url, "url");
        try {
            return new URL(url);
        }
        catch (MalformedURLException ex) {
            logger.trace("invalid URL", ex);
            return null;
        }
    }

    private Misc() {
    }
}
