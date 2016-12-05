package info.naiv.lab.java.jmt.iteration;

import static info.naiv.lab.java.jmt.Misc.nop;
import info.naiv.lab.java.jmt.fx.Consumer1;
import info.naiv.lab.java.jmt.fx.Consumer2;
import info.naiv.lab.java.jmt.fx.Function1;
import info.naiv.lab.java.jmt.fx.Function2;
import info.naiv.lab.java.jmt.fx.Predicate1;
import info.naiv.lab.java.jmt.fx.StandardFunctions;
import info.naiv.lab.java.jmt.monad.Iteratee;
import info.naiv.lab.java.jmt.monad.IterateeImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import lombok.NonNull;

/**
 *
 * @author enlo
 */
public class IterationUtils {

    /**
     *
     * @param <T>
     * @param iter
     * @param n
     * @return
     */
    public static <T> boolean advance(Iterator<T> iter, int n) {
        int i = 0;
        for (; i < n && iter.hasNext(); i++) {
            iter.next();
        }
        return i == n;
    }

    /**
     * コレクションから受け入れ可能なものを取得
     *
     * @param <T> 要素の型
     * @param items コレクションから等価であるものを取得
     * @param predicate 検索
     * @return 等価な項目があれば true.
     */
    public static <T> boolean contains(Iterable<T> items, @NonNull Predicate1<? super T> predicate) {
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

    public static <T> long count(Iterator<T> iter) {
        long r = 0;
        while (iter.hasNext()) {
            iter.next();
            r++;
        }
        return r;
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
        return new IterateeImpl<>(iterable, predicate);
    }

    /**
     *
     * フィルター
     *
     * @param <T>
     * @param iter
     * @param predicate
     * @return
     */
    @Nonnull
    public static <T> Iterator<T> filter(Iterator<T> iter, Predicate1<? super T> predicate) {
        return new FilteringIterator<>(iter, predicate);
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
        return new IterateeImpl<>(iterable, StandardFunctions.<T>nonNull());
    }

    /**
     *
     * @param <T>
     * @param items
     * @return
     */
    @Nonnull
    public static <T> Iterable<T> flat(final Iterable<? extends Iterable<T>> items) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new FlatIterableIterator<>(items);
            }
        };
    }

    /**
     *
     * @param <T>
     * @param iter
     * @param action
     */
    public static <T> void forEach(Iterable<T> iter, @NonNull final Consumer1<? super T> action) {
        forEach(iter, new LoopAction<T>() {
            @Override
            public void accept(T a1, LoopCondition a2) {
                action.accept(a1);
            }
        });
    }

    /**
     *
     * @param <T>
     * @param iter
     * @param action
     */
    public static <T> void forEach(Iterable<T> iter, @NonNull Consumer2<? super T, LoopCondition> action) {
        if (iter != null) {
            LoopConditionImpl c = new LoopConditionImpl();
            try {
                for (T it : iter) {
                    try {
                        action.accept(it, c);
                    }
                    catch (ContinueException exc) {
                        nop(exc);
                    }
                    c.next();
                }
            }
            catch (BreakException exb) {
                nop(exb);
            }
        }
    }

    /**
     *
     * @param <T>
     * @param <R>
     * @param iter
     * @param func
     * @return
     */
    public static <T, R> List<R> forEach(Iterable<T> iter, @NonNull final Function1<? super T, R> func) {
        return forEach(iter, new LoopFunction<T, R>() {
            @Override
            public R apply(T a1, LoopCondition a2) {
                return func.apply(a1);
            }
        });
    }

    /**
     *
     * @param <T>
     * @param <R>
     * @param iter
     * @param func
     * @return
     */
    public static <T, R> List<R> forEach(Iterable<T> iter,
                                         @NonNull final Function2<? super T, LoopCondition, R> func) {
        final List<R> result;
        if (iter instanceof Collection) {
            result = new ArrayList<>(((Collection) iter).size());
        }
        else {
            result = new ArrayList<>();
        }
        forEach(iter, new LoopAction<T>() {
            @Override
            public void accept(T a1, LoopCondition a2) {
                result.add(func.apply(a1, a2));
            }
        });
        return result;
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
    @CheckForNull
    public static <T> T getFirst(Iterable<T> iterable, @NonNull Predicate1<? super T> predicate) {
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
     * 中身があるかどうかチェック.
     *
     * @param object チェックするオブジェクト
     * @return 空でなければ true.
     */
    public static boolean isNotEmpty(Iterable<?> object) {
        return !isEmpty(object);
    }

    /**
     * 反復子の値を変換する.
     *
     * @param <T>
     * @param <U>
     * @param iter
     * @param mapper
     * @return
     */
    @Nonnull
    public static <T, U> Iterable<U> map(final Iterable<T> iter, final Function1<? super T, ? extends U> mapper) {
        return new Iterable<U>() {
            @Override
            public Iterator<U> iterator() {
                return new MappingIterator<>(iter.iterator(), mapper);
            }
        };
    }

    private IterationUtils() {
    }

    private static class LoopConditionImpl extends LoopCondition {

        private static final long serialVersionUID = 1L;

        int index = 0;

        @Override
        public int index() {
            return index;
        }

        public void next() {
            index++;
        }
    }

}
