package info.naiv.lab.java.jmt;

import static info.naiv.lab.java.jmt.Arguments.nonEmpty;
import static info.naiv.lab.java.jmt.Arguments.nonNull;
import static info.naiv.lab.java.jmt.Constants.ZWNBSP;
import info.naiv.lab.java.jmt.datetime.ClassicDateUtils;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.parseCalendar;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.io.NIOUtils;
import info.naiv.lab.java.jmt.iterator.FlatIterableIterator;
import info.naiv.lab.java.jmt.iterator.MappingIterator;
import info.naiv.lab.java.jmt.mark.Nop;
import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import info.naiv.lab.java.jmt.monad.Iteratee;
import info.naiv.lab.java.jmt.monad.IterateeImpl;
import info.naiv.lab.java.jmt.monad.Optional;
import info.naiv.lab.java.jmt.monad.OptionalImpl;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.Character.isSpaceChar;
import static java.lang.Character.isWhitespace;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

/**
 *
 * @author enlo
 */
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

    /**
     *
     * @param <T>
     * @param iter
     * @param n
     * @return
     */
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
     * @return from &lt;= value &lt;= to
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
     * compareTo を利用して同一の値かどうかを検証する。 どちらかの値が null なら false を戻す.
     *
     * @param <T> 値の型
     * @param lhs 左辺値
     * @param rhs 右辺値
     * @return どちらかの値が null なら false, そうでなければ lhs.compareTo(rhs) == 0
     */
    public static <T extends Comparable<T>> boolean compareEqual(T lhs, T rhs) {
        if (lhs == null || rhs == null) {
            return false;
        }
        else {
            return lhs.compareTo(rhs) == 0;
        }
    }

    /**
     * 文字列を連結する.
     *
     * @param items
     * @return
     */
    public static String concatnate(String... items) {
        int size = 0;
        for (String item : items) {
            if (item != null) {
                size += item.length();
            }
        }
        CharBuffer buffer = CharBuffer.allocate(size);
        for (String item : items) {
            if (item != null) {
                buffer.append(item);
            }
        }
        buffer.flip();
        return buffer.toString();
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
    public static <T extends Comparable<T>> boolean containsCompareEquals(Iterable<? extends T> items, T valueToFind) {
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
    public static <T> boolean equal(T lhs, T rhs) {
        return (lhs == rhs) || ((lhs != null) && lhs.equals(rhs));
    }

    /**
     *
     * @param <T>
     * @param iterable
     * @param predicate
     * @return
     */
    @ReturnNonNull
    public static <T> Iteratee<T> filter(Iterable<T> iterable, Predicate1<T> predicate) {
        return new IterateeImpl<>(iterable, predicate);
    }

    /**
     *
     * @param <T>
     * @param items
     * @return
     */
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
     * バイト列を文字列にフォーマットする.
     *
     * @param data バイト列.
     * @param byteFormat 各バイトごとのフォーマット.
     * @return フォーマット済み文字列.
     */
    public static String formatBytes(byte[] data, String byteFormat) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : data) {
                formatter.format(byteFormat, b);
            }
            return formatter.toString();
        }
    }

    /**
     * 最初の項目または null を戻す.
     *
     * @param <T>
     * @param iterable
     * @return 最初の項目. 空なら null.
     */
    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable != null) {
            for (T i : iterable) {
                return i;
            }
        }
        return null;
    }

    /**
     * 条件に一致する最初の項目、または null を戻す.
     *
     * @param <T>
     * @param iterable
     * @param predicate
     * @return
     */
    public static <T> T getFirst(Iterable<T> iterable, Predicate1<T> predicate) {
        nonNull(predicate, "predicate");
        if (iterable != null) {
            for (T i : iterable) {
                if (predicate.test(i)) {
                    return i;
                }
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
    public static boolean isBlank(CharSequence object) {
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
    public static boolean isEmpty(CharSequence object) {
        return object == null || object.length() == 0;
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
    public static <Dest extends Collection<R>, R, T> Dest map(Dest dest, Collection<T> source, Function1<? super T, R> mapper) throws IllegalArgumentException {
        nonNull(dest, "dest");
        nonNull(mapper, "mapper");
        if (source != null) {
            for (T item : source) {
                dest.add(mapper.apply(item));
            }
        }
        return dest;
    }

    /**
     *
     * @param <T>
     * @param <U>
     * @param iter
     * @param mapper
     * @return
     */
    @ReturnNonNull
    public static <T, U> Iterable<U> map(final Iterable<T> iter, final Function1<? super T, ? extends U> mapper) {
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
     * [min ～ max] 内の値を戻す.
     *
     * @param <T>
     * @param x 値
     * @param min 最小値
     * @param max 最大値
     * @return min ≦ x ≦ max の値
     */
    public static <T extends Comparable<T>> T minmax(T x, T min, T max) {
        if (min != null && x.compareTo(min) <= 0) {
            return min;
        }
        else if (max != null && max.compareTo(x) <= 0) {
            return max;
        }
        else {
            return x;
        }
    }

    /**
     * ArrayList を作成.
     *
     * @param <T>
     * @param values
     * @return
     */
    public static <T> ArrayList<T> newArrayList(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> Optional<T> newInstance(Optional<Class<T>> clazz) {
        if (clazz.isPresent()) {
            return OptionalImpl.ofNullable(newInstance(clazz.get()));
        }
        else {
            return OptionalImpl.<T>empty();
        }
    }

    /**
     *
     * @param className
     * @return
     */
    public static Optional<Object> newInstance(String className) {
        Optional<Class<?>> clazz = resolveClassName(className);
        return newInstance((Optional) clazz);
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
     * 改行文字を正規化.
     *
     * @param text テキスト
     * @return 全ての改行文字を CRLF にしたテキスト
     */
    public static String normalizeLineSeparator(String text) {
        return normalizeLineSeparator(text, Constants.CRLF);
    }

    /**
     * 改行文字を正規化.
     *
     * @param text テキスト
     * @param ls 改行文字
     * @return 全ての改行文字を ls にしたテキスト
     */
    public static String normalizeLineSeparator(String text, String ls) {
        return text.replaceAll("\\u000D\\u000A|\\u000A|\\u000D|\\u0085|\\u0008|\\u000C|\\u2028|\\u2029", ls);
    }

    /**
     * オブジェクトを double に変換する.
     *
     * @param value 数値に変換したいオブジェクト.
     * @param defaultValue 数値に変換できない場合の既定値.
     * @return 数値オブジェクト
     */
    public static double objectToDouble(Object value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        else {
            return toNumber(value.toString(), defaultValue).doubleValue();
        }
    }

    /**
     * オブジェクトを int に変換する.
     *
     * @param value 数値に変換したいオブジェクト.
     * @param defaultValue 数値に変換できない場合の既定値.
     * @return 数値オブジェクト
     */
    public static int objectToInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        else {
            return toInt(value.toString(), defaultValue);
        }
    }

    /**
     * オブジェクトを long に変換する.
     *
     * @param value 数値に変換したいオブジェクト.
     * @param defaultValue 数値に変換できない場合の既定値.
     * @return 数値オブジェクト
     */
    public static long objectToLong(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        else if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        else {
            return toLong(value.toString(), defaultValue);
        }
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
     * クラスを読み込む.
     *
     * @param className クラス名
     * @return クラスをOptionalでラップしたもの.
     */
    public static Optional<Class<?>> resolveClassName(String className) {
        try {
            return OptionalImpl.<Class<?>>ofNullable(Class.forName(className));
        }
        catch (ClassNotFoundException ex) {
            logger.debug("class load failed. ", ex);
            return OptionalImpl.<Class<?>>empty();
        }
    }

    /**
     * クラスを読み込む.
     *
     * @param className クラス名
     * @param initialize 初期化するかどうか.
     * @param classLoader クラスローダー.
     * @return クラスをOptionalでラップしたもの.
     */
    public static Optional<Class<?>> resolveClassName(String className, boolean initialize, ClassLoader classLoader) {
        try {
            Class clz = Class.forName(className, initialize, classLoader);
            return OptionalImpl.<Class<?>>ofNullable(clz);
        }
        catch (ClassNotFoundException ex) {
            logger.debug("class load failed. ", ex);
            return OptionalImpl.<Class<?>>empty();
        }
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

    /**
     *
     * @param source
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(Boolean source, boolean defaultValue) {
        return source != null ? source : defaultValue;
    }

    /**
     *
     * @param source
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(String source, boolean defaultValue) {
        return source != null ? Boolean.parseBoolean(source) : defaultValue;
    }

    /**
     *
     * @param source
     * @param defaultValue
     * @return
     */
    public static boolean toBoolean(Number source, boolean defaultValue) {
        return source != null ? source.doubleValue() != 0 : defaultValue;
    }

    /**
     *
     * @param text
     * @param charset
     * @return
     */
    @ReturnNonNull
    public static byte[] toByteArray(String text, String charset) {
        if (text == null) {
            return new byte[]{};
        }
        Charset cs = CharsetMap.get(charset);
        return text.getBytes(cs);
    }

    /**
     *
     * @param resource
     * @return
     */
    @ReturnNonNull
    public static byte[] toByteArray(Resource resource) {
        if (resource == null) {
            return new byte[]{};
        }
        try (InputStream io = resource.getInputStream()) {
            return NIOUtils.toByteArray(io);
        }
        catch (IOException ex) {
            logger.warn("resource open failed.", ex);
            return new byte[]{};
        }
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

    /**
     *
     * @param source
     * @param defaultValue
     * @return
     */
    public static Double toDoubleNum(Number source, Double defaultValue) {
        if (source != null) {
            return source.doubleValue();
        }
        else {
            return defaultValue;
        }
    }

    /**
     *
     * @param source
     * @param defaultValue
     * @return
     */
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
     * <dt>{@link BigDecimal#getClass()}</dt>
     * <dd>BigDecimal として処理.<dd>
     * <dt>{@link Long#getClass()}</dt>
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

    /**
     * 文字列をURLに変更する.
     *
     * @param url URL文字列.
     * @return URL. URLの形式が不正であれば、null.
     */
    public static URL toURL(String url) {
        nonEmpty(url, "url");
        try {
            return new URL(url);
        }
        catch (MalformedURLException ex) {
            logger.trace(url, ex);
            return null;
        }
    }

    private Misc() {
    }
}
