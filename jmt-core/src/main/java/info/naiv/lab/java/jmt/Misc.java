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
package info.naiv.lab.java.jmt;

import info.naiv.lab.java.jmt.bean.PDBasedBeanCopierFactory;
import static info.naiv.lab.java.jmt.Arguments.nonEmpty;
import static info.naiv.lab.java.jmt.Constants.ZWNBSP;
import info.naiv.lab.java.jmt.datetime.ClassicDateUtils;
import static info.naiv.lab.java.jmt.datetime.ClassicDateUtils.parseCalendar;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Consumer2;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.i18n.Locales;
import info.naiv.lab.java.jmt.infrastructure.ServiceProvider;
import static info.naiv.lab.java.jmt.infrastructure.ServiceProviders.getThreadContainer;
import info.naiv.lab.java.jmt.infrastructure.Tag;
import info.naiv.lab.java.jmt.io.NIOUtils;
import info.naiv.lab.java.jmt.iteration.BreakException;
import info.naiv.lab.java.jmt.iteration.ContinueException;
import info.naiv.lab.java.jmt.iteration.IterationUtils;
import info.naiv.lab.java.jmt.iteration.LoopCondition;
import info.naiv.lab.java.jmt.mark.Nop;
import info.naiv.lab.java.jmt.monad.Iteratee;
import info.naiv.lab.java.jmt.monad.Optional;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import static java.lang.Character.isSpaceChar;
import static java.lang.Character.isWhitespace;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
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
import java.util.IllformedLocaleException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.util.SerializationUtils;

/**
 *
 * @author enlo
 */
@Slf4j
public class Misc {

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
    @Nonnull
    public static <T> Iterator<T> advance(@Nonnull Iterator<T> iter, int n) {
        IterationUtils.advance(iter, n);
        return iter;
    }

    /**
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static int asInt(Object value, int defaultValue) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        else if (value instanceof CharSequence) {
            return toInt(value.toString(), defaultValue);
        }
        return defaultValue;
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
     * 直列化を使用したクローン.
     *
     * @param <T>
     * @param object
     * @return
     */
    public static <T extends Serializable> T cloneThroughSerialize(T object) {
        byte[] data = SerializationUtils.serialize(object);
        return (T) SerializationUtils.deserialize(data);
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
    @Nonnull
    public static String concatnate(String... items) {
        return Strings.concatnate(items);
    }

    /**
     * コレクションから受け入れ可能なものを取得
     *
     * @param <T> 要素の型
     * @param items コレクションから等価であるものを取得
     * @param predicate 検索
     * @return 等価な項目があれば true.
     */
    public static <T> boolean contains(Iterable<T> items, @Nonnull Predicate1<? super T> predicate) {
        return IterationUtils.contains(items, predicate);
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
        return IterationUtils.containsCompareEquals(items, valueToFind);
    }

    /**
     * Bean から Bean へプロパティをコピーする.
     *
     * @param source
     * @param dest
     * @param ignoreProperties
     */
    public static void copyProperties(@NonNull Object source, @NonNull Object dest, String... ignoreProperties) {
        Class<?> sourceType = source.getClass();
        Class<?> destType = dest.getClass();
        copyProperties(sourceType, destType, source, dest, ignoreProperties);
    }

    /**
     * Bean から Bean へプロパティをコピーする.
     *
     * @param sourceType
     * @param destType
     * @param source
     * @param dest
     * @param ignoreProperties
     */
    public static void copyProperties(Class<?> sourceType, Class<?> destType, Object source, Object dest, String... ignoreProperties) {
        BeanCopier copier = getBeanCopier(sourceType, destType, ignoreProperties);
        copier.copyProperties(source, dest);
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
     * フィルター
     *
     * @param <T>
     * @param iterable
     * @param predicate
     * @return
     */
    @Nonnull
    public static <T> Iteratee<T> filter(Iterable<T> iterable, Predicate1<? super T> predicate) {
        return IterationUtils.filter(iterable, predicate);
    }

    /**
     *
     * null を排除したフィルター
     *
     * @param <T>
     * @param iterable
     * @return
     */
    @Nonnull
    public static <T> Iteratee<T> filterNonNull(Iterable<T> iterable) {
        return IterationUtils.filterNonNull(iterable);
    }

    /**
     *
     * @param <T>
     * @param items
     * @return
     */
    @Nonnull
    public static <T> Iterable<T> flat(final Iterable<? extends Iterable<T>> items) {
        return IterationUtils.flat(items);
    }

    /**
     * ForEach. Java8では不要. <br>
     * 処理の途中で{@link BreakException}を投げると break と同じ効果があり、<br>
     * 処理の途中で{@link ContinueException}を投げると break と同じ効果.
     *
     * @param <T>
     * @param iter
     * @param action
     */
    public static <T> void forEach(Iterable<T> iter, @Nonnull Consumer1<? super T> action) {
        IterationUtils.forEach(iter, action);
    }

    /**
     * ForEach. Java8では不要.
     *
     * @param <T>
     * @param iter
     * @param action
     */
    public static <T> void forEach(Iterable<T> iter, @Nonnull Consumer2<? super T, LoopCondition> action) {
        IterationUtils.forEach(iter, action);
    }

    /**
     * バイト列を文字列にフォーマットする.
     *
     * @param data バイト列.
     * @param byteFormat 各バイトごとのフォーマット.
     * @return フォーマット済み文字列.
     */
    @Nonnull
    public static String formatBytes(byte[] data, @NonNull String byteFormat) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : data) {
                formatter.format(byteFormat, b);
            }
            return formatter.toString();
        }
    }

    /**
     * BeanCopier を取得する.
     *
     * @param <TSource>
     * @param <TDest>
     * @param sourceType
     * @param destType
     * @param ignoreProperties
     * @return
     */
    public static <TSource, TDest>
            BeanCopier<TSource, TDest> getBeanCopier(Class<TSource> sourceType,
                                                     Class<TDest> destType,
                                                     String... ignoreProperties) {
        return PDBasedBeanCopierFactory
                .createInstance(sourceType, destType, ignoreProperties);
    }

    /**
     * 最初の項目または null を戻す.
     *
     * @param <T>
     * @param iterable
     * @return 最初の項目. 空なら null.
     */
    @CheckForNull
    public static <T> T getFirst(Iterable<T> iterable) {
        return IterationUtils.getFirst(iterable);
    }

    /**
     * 条件に一致する最初の項目、または null を戻す.
     *
     * @param <T>
     * @param iterable
     * @param predicate
     * @return
     */
    @CheckForNull
    public static <T> T getFirst(Iterable<T> iterable, @Nonnull Predicate1<? super T> predicate) {
        return IterationUtils.getFirst(iterable, predicate);
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
    @Nonnull
    public static <TKey, TValue> Set<TKey> getKeySetByValue(@NonNull Map<TKey, TValue> map, TValue value) {
        Set<TKey> keys = new HashSet<>();
        for (Entry<TKey, TValue> e : map.entrySet()) {
            if (Objects.equals(e.getValue(), value)) {
                keys.add(e.getKey());
            }
        }
        return keys;
    }

    /**
     *
     * @param <T>
     * @param list
     * @param index
     * @param defaultValue
     * @return
     */
    public static <T> T getOrDefault(List<T> list, int index, T defaultValue) {
        if (list != null && 0 <= index && index < list.size()) {
            return list.get(index);
        }
        return defaultValue;
    }

    /**
     *
     * @param <T>
     * @param array
     * @param index
     * @param defaultValue
     * @return
     */
    public static <T> T getOrDefault(T[] array, int index, T defaultValue) {
        if (array != null && 0 <= index && index < array.length) {
            return array[index];
        }
        return defaultValue;
    }

    /**
     * 中身が空白かどうかチェック.
     *
     * @param object チェックする文字列.
     * @return 空か空白のみなら true.
     * @see Character#isWhitespace(char)
     */
    public static boolean isBlank(CharSequence object) {
        return Strings.isBlank(object);
    }

    /**
     * 中身が空かどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空ならば true.
     */
    public static boolean isEmpty(CharSequence object) {
        return Strings.isEmpty(object);
    }

    /**
     * 中身が空かどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空ならば true.
     */
    public static boolean isEmpty(Iterable<?> object) {
        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        }
        else if (object instanceof Map) {
            return ((Map) object).isEmpty();
        }
        return IterationUtils.isEmpty(object);
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
    public static boolean isLoadable(@Nonnull String className) {
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
    public static boolean isLoadable(@Nonnull String className, ClassLoader loader) {
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
    public static boolean isNotEmpty(CharSequence object) {
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
    @Nonnull
    public static String join(@Nonnull Iterable<?> items, String delim) {
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
    @Nonnull
    public static <Dest extends Collection<R>, R, T>
            Dest map(@NonNull Dest dest, Collection<T> source, @Nonnull Function1<? super T, R> mapper) {
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
    @Nonnull
    public static <T, U> Iterable<U> map(final Iterable<T> iter, @Nonnull final Function1<? super T, U> mapper) {
        return IterationUtils.<T, U>map(iter, mapper);
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
    @Nonnull
    public static <Dest extends Map<K, R>, K, R, T>
            Dest map(@NonNull Dest dest, Map<K, T> source, @Nonnull Function1<? super T, R> mapper) {
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
    @Nonnull
    public static <R, T> Set<R> map(Set<T> source, @Nonnull Function1<? super T, R> mapper) {
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
    @Nonnull
    public static <R, T> List<R> map(List<T> source, @Nonnull Function1<? super T, R> mapper) {
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
    @Nonnull
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
    @Nonnull
    public static <T> ArrayList<T> newArrayList(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }

    /**
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @CheckForNull
    public static <T> T newInstance(@Nonnull Class<T> clazz) {
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
    @Nonnull
    public static <T> Optional<T> newInstance(@Nonnull Optional<Class<T>> clazz) {
        if (clazz.isPresent()) {
            return Optional.ofNullable(newInstance(clazz.get()));
        }
        else {
            return Optional.<T>empty();
        }
    }

    /**
     *
     * @param className
     * @return
     */
    @Nonnull
    public static Optional<Object> newInstance(@Nonnull String className) {
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
    @Nonnull
    public static String normalizeLineSeparator(@NonNull String text, @Nonnull String ls) {
        return text.replaceAll("\\u000D\\u000A|\\u000A|\\u000D|\\u0085|\\u0008|\\u000C|\\u2028|\\u2029", ls);
    }

    /**
     * null を戻す. null をキャストする際の警告対策.
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> T nullOf(@Nonnull Class<T> clazz) {
        return null;
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
     * @param value 文字列
     * @return 先頭から ZWNBSP を除外した文字列.
     */
    public static String removeByteOrderMark(String value) {
        if (isEmpty(value)) {
            return value;
        }
        else if (value.startsWith(ZWNBSP)) {
            return value.substring(ZWNBSP.length());
        }
        else {
            return value;
        }
    }

    /**
     * 先頭の ZWNBSP (BOM) を除外. use {@link #removeByteOrderMark(java.lang.String) }
     *
     * @param value 文字列
     * @return 先頭から ZWNBSP を除外した文字列.
     */
    @Deprecated
    public static String removeZwnbsp(String value) {
        return removeByteOrderMark(value);
    }

    /**
     * 繰り返し値を取得する.
     *
     * @param <T> 項目の型
     * @param repeatMax 繰り返す回数
     * @param value 値
     * @return Iterable
     */
    @Nonnull
    public static <T> Iterable<T> repeat(int repeatMax, T value) {
        return new Repeater<>(repeatMax, value);
    }

    /**
     * クラスを読み込む.
     *
     * @param className クラス名
     * @return クラスをOptionalでラップしたもの.
     */
    @Nonnull
    public static Optional<Class<?>> resolveClassName(@Nonnull String className) {
        try {
            return Optional.<Class<?>>ofNullable(Class.forName(className));
        }
        catch (ClassNotFoundException ex) {
            logger.debug("class load failed. className:{} throws:{}", className, ex.getMessage());
            return Optional.<Class<?>>empty();
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
    @Nonnull
    public static Optional<Class<?>> resolveClassName(@Nonnull String className, boolean initialize, ClassLoader classLoader) {
        try {
            Class clz = Class.forName(className, initialize, classLoader);
            return Optional.<Class<?>>ofNullable(clz);
        }
        catch (ClassNotFoundException ex) {
            logger.debug("class load failed. className:{} throws:{}", className, ex.getMessage());
            return Optional.<Class<?>>empty();
        }
    }

    /**
     *
     * @param text
     * @param delim
     * @param trim
     * @return
     */
    @CheckForNull
    public static KeyValuePair<String, String> splitKeyValue(String text, String delim, boolean trim) {
        nonEmpty(delim, "delim");
        if (isEmpty(text)) {
            return null;
        }
        int index = text.indexOf(delim);
        if (index < 0) {
            return null;
        }
        String key = text.substring(0, index);
        String value = text.substring(index + delim.length());
        if (trim) {
            return KeyValuePair.of(key.trim(), value.trim());
        }
        else {
            return KeyValuePair.of(key, value);
        }
    }

    /**
     * 文字配列を文字列化する.
     *
     * @param chars
     * @return
     */
    @Nonnull
    public static String stringize(char[] chars) {
        return chars != null ? new String(chars) : "";
    }

    /**
     * オブジェクトを文字列化する.
     *
     * @param obj
     * @return
     */
    @Nonnull
    public static String stringize(Object obj) {
        return obj != null ? obj.toString() : "";
    }

    /**
     * バイト列をエンコード指定して文字列化する.
     *
     * @param data
     * @param cs
     * @return
     */
    @Nonnull
    public static String stringize(byte[] data, Charset cs) {
        if (data == null) {
            return "";
        }
        return stringize(ByteBuffer.wrap(data), cs);
    }

    /**
     * バッファをエンコード指定して文字列化する.
     *
     * @param data
     * @param cs
     * @return
     */
    @Nonnull
    public static String stringize(ByteBuffer data, Charset cs) {
        if (data == null) {
            return "";
        }
        if (cs == null) {
            cs = Charset.defaultCharset();
        }
        return cs.decode(data).toString();
    }

    /**
     * オブジェクトをフォーマット指定して文字列化する.
     *
     * @param obj
     * @param format
     * @return
     */
    @Nonnull
    public static String stringize(Object obj, Format format) {
        if (obj == null) {
            return "";
        }
        if (format == null) {
            return obj.toString();
        }
        return format.format(obj);
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
                logger.warn("to bigdecimal error. source:{}, throws:{}", source, e.getMessage());
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
    @Nonnull
    public static byte[] toByteArray(String text, String charset) {
        if (text == null) {
            return new byte[]{};
        }
        Charset cs = Charset.forName(charset);
        return text.getBytes(cs);
    }

    /**
     *
     * @param resource
     * @return
     */
    @Nonnull
    public static byte[] toByteArray(Resource resource) {
        if (resource == null) {
            return new byte[]{};
        }
        try (InputStream io = resource.getInputStream()) {
            return NIOUtils.toByteArray(io);
        }
        catch (IOException ex) {
            logger.warn("resource open failed. {}", ex.getMessage());
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
                logger.warn("parse error. {}", ex.getMessage());
            }
        }
        return defaultValue;
    }

    /**
     * CharSequence を char配列に変換.
     *
     * @param chars
     * @return
     */
    @Nonnull
    public static char[] toCharArray(CharSequence chars) {
        if (chars == null) {
            return new char[]{};
        }
        if (chars instanceof String) {
            return ((String) chars).toCharArray();
        }
        int sz = chars.length();
        char[] chz = new char[sz];
        for (int i = 0; i < sz; i++) {
            chz[i] = chars.charAt(i);
        }
        return chz;
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
                logger.warn("parse error. {}", ex.getMessage());
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
    @Nonnull
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
     * オブジェクトをロケールに変換する.
     *
     * @param locale 文字列またはロケールオブジェクト.
     * @param defaultLocale 既定のロケール.
     * @return
     */
    public static Locale toLocale(Object locale, Locale defaultLocale) {
        if (locale instanceof CharSequence) {
            String localeString = locale.toString();
            try {
                return Locales.toLocale(localeString);
            }
            catch (IllformedLocaleException ex) {
                logger.warn("illformed locale. {}", localeString, ex);
                return defaultLocale;
            }
        }
        else if (locale instanceof Locale) {
            return (Locale) locale;
        }
        else if (locale instanceof Locale.Builder) {
            return ((Locale.Builder) locale).build();
        }
        return defaultLocale;
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
                logger.warn("toNumber error. {}", ex.getMessage());
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
    @Nonnull
    public static String toString(byte[] bytes, String charset) {
        Charset cs = CharsetMap.get(charset);
        return new String(bytes, cs);
    }

    /**
     * バイト列から文字列を作成する.
     *
     * @param bytes
     * @param offset
     * @param length
     * @param charset
     * @return
     */
    @Nonnull
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
        try {
            return stringize(value, format);
        }
        catch (Exception e) {
            logger.warn("format failed. value:{} format:{} throws:{}", value, format, e.getMessage());
            return defaultValue;
        }
    }

    /**
     *
     * @param objects
     * @return
     */
    public static List<String> toStringList(Iterable<?> objects) {
        ArrayList<String> result = new ArrayList<>();
        if (objects != null) {
            if (objects instanceof Collection) {
                result.ensureCapacity(((Collection) objects).size());
            }
            for (Object obj : objects) {
                result.add(Objects.toString(obj));
            }
        }
        return result;
    }

    /**
     * 文字列をURLに変更する.
     *
     * @param url URL文字列.
     * @return URL. URLの形式が不正であれば、null.
     */
    public static URL toURL(@Nonnull String url) {
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
