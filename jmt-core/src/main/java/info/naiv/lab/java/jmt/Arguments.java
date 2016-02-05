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

import info.naiv.lab.java.jmt.mark.ReturnNonNull;
import java.util.Collection;
import static info.naiv.lab.java.jmt.ClassicArrayUtils.arrayToString;

/**
 *
 * @author enlo
 */
public abstract class Arguments {

    /**
     * 引数の範囲チェック.
     *
     * @param <T> 比較可能な型
     * @param arg チェックする値.
     * @param begin 下限値
     * @param end 上限値
     * @param varname 引数名.
     * @return arg
     * @throws IllegalArgumentException arg が範囲外.
     */
    public static <T extends Comparable<T>> T between(T arg, T begin, T end, String varname) throws IllegalArgumentException {
        if (begin.compareTo(arg) > 0 || end.compareTo(arg) < 0) {
            String msg = arrayToString(varname, " is must be between ", begin, " and ", end, ".");
            throw new IllegalArgumentException(msg);
        }
        return arg;
    }

    /**
     * 引数の範囲チェック.
     *
     * @param arg チェックする値.
     * @param begin 下限値
     * @param end 上限値
     * @param varname 引数名.
     * @return arg
     * @throws IllegalArgumentException arg が範囲外.
     */
    public static long between(long arg, long begin, long end, String varname) throws IllegalArgumentException {
        if (arg < begin || end < arg) {
            String msg = arrayToString(varname, " is must be between ", begin, " and ", end, ".");
            throw new IllegalArgumentException(msg);
        }
        return arg;
    }

    /**
     * 引数の範囲チェック.
     *
     * @param arg チェックする値.
     * @param begin 下限値
     * @param end 上限値
     * @param varname 引数名.
     * @return arg
     * @throws IllegalArgumentException arg が範囲外.
     */
    public static int between(int arg, int begin, int end, String varname) throws IllegalArgumentException {
        if (arg < begin || end < arg) {
            String msg = arrayToString(varname, " is must be between ", begin, " and ", end, ".");
            throw new IllegalArgumentException(msg);
        }
        return arg;
    }

    /**
     * 引数の範囲チェック.
     *
     * @param arg チェックする値.
     * @param begin 下限値
     * @param end 上限値
     * @param varname 引数名.
     * @return arg
     * @throws IllegalArgumentException arg が範囲外.
     */
    public static double between(double arg, double begin, double end, String varname) throws IllegalArgumentException {
        if (arg < begin || end < arg) {
            String msg = arrayToString(varname, " is must be between ", begin, " and ", end, ".");
            throw new IllegalArgumentException(msg);
        }
        return arg;
    }

    /**
     * 引数が特定の型のインスタンスかどうかチェック.
     *
     * @param <T> 型
     * @param arg チェックするオブジェクト
     * @param clazz 型情報
     * @param varname 引数名.
     * @return arg.
     * @throws IllegalArgumentException 引数が特定の型のインスタンスではない.
     */
    public static <T> T isInstanceOf(Object arg, Class<T> clazz, String varname) throws IllegalArgumentException {
        if (arg != null && !clazz.isInstance(arg)) {
            throw new IllegalArgumentException(varname + " is must be interface.");
        }
        return clazz.cast(arg);
    }

    /**
     * 引数がインターフェイスかどうかチェック.
     *
     * @param <T> 型
     * @param arg チェックするクラス
     * @param varname 引数名
     * @return arg
     * @throws IllegalArgumentException arg がインターフェイスではない.
     */
    @ReturnNonNull
    public static <T> Class<T> isInterface(Class<T> arg, String varname) throws IllegalArgumentException {
        if (arg == null || !arg.isInterface()) {
            throw new IllegalArgumentException(varname + " is must be interface.");
        }
        return arg;
    }

    /**
     * lhs &lt; rhs を満たすかどうかチェック.
     *
     * @param <T>
     * @param lhs
     * @param rhs
     * @param varname
     * @return
     */
    public static <T extends Comparable<T>> T lessThan(T lhs, T rhs, String varname) {
        nonNull(lhs, varname);
        if (lhs.compareTo(rhs) < 0) {
            return lhs;
        }
        throw new IllegalArgumentException(varname + " is must less than " + rhs);
    }

    /**
     * lhs &lt; rhs を満たすかどうかチェック.
     *
     * @param lhs
     * @param rhs
     * @param varname
     * @return
     */
    public static long lessThan(long lhs, long rhs, String varname) {
        if (lhs < rhs) {
            return lhs;
        }
        throw new IllegalArgumentException(varname + " is must less than " + rhs);
    }

    /**
     * lhs &lt; rhs を満たすかどうかチェック.
     *
     * @param lhs
     * @param rhs
     * @param varname
     * @return
     */
    public static int lessThan(int lhs, int rhs, String varname) {
        if (lhs < rhs) {
            return lhs;
        }
        throw new IllegalArgumentException(varname + " is must less than " + rhs);
    }

    /**
     * 引数の null または空 チェック.
     *
     * @param arg チェックする引数.
     * @param varname 引数名
     * @throws IllegalArgumentException arg が null または empty.
     * @return arg.
     */
    @ReturnNonNull
    public static String nonEmpty(String arg, String varname) throws IllegalArgumentException {
        if (arg == null || arg.isEmpty()) {
            throw new IllegalArgumentException(varname + " is must not be empty.");
        }
        return arg;
    }

    /**
     * コレクションの null または empty チェック.
     *
     * @param <T> コレクションの型.
     * @param arg チェックするコレクション.
     * @param varname 引数名.
     * @return arg.
     * @throws IllegalArgumentException arg が null または empty.
     */
    @ReturnNonNull
    public static <T extends Collection> T nonEmpty(T arg, String varname) throws IllegalArgumentException {
        if (arg == null || arg.isEmpty()) {
            throw new IllegalArgumentException(varname + "is must not be empty.");
        }
        return arg;
    }

    /**
     * 配列の null または empty チェック.
     *
     * @param <T> 配列の型.
     * @param arg チェックするコレクション.
     * @param varname 引数名.
     * @return arg.
     * @throws IllegalArgumentException arg が null または empty.
     */
    @ReturnNonNull
    public static <T> T[] nonEmpty(T[] arg, String varname) throws IllegalArgumentException {
        if (arg == null || arg.length == 0) {
            throw new IllegalArgumentException(varname + "is must not be empty.");
        }
        return arg;
    }

    /**
     * 引数がマイナスでないことをチェック.
     *
     * @param arg チェックする値.
     * @param varname 引数名.
     * @return arg
     * @throws IllegalArgumentException arg が範囲外.
     */
    public static long nonMinus(long arg, String varname) throws IllegalArgumentException {
        if (arg < 0) {
            throw new IllegalArgumentException(varname + " is must be plus.");
        }
        return arg;
    }

    /**
     * 引数の null チェック.
     *
     * @param <T>
     * @param arg チェックする引数.
     * @param varname 引数名
     * @throws IllegalArgumentException arg が null.
     * @return arg.
     */
    @ReturnNonNull
    public static <T> T nonNull(T arg, String varname) throws IllegalArgumentException {
        if (arg == null) {
            throw new IllegalArgumentException(varname + " is must not be null.");
        }
        return arg;
    }

    /**
     * 配列の全項目が null でないことをチェックする.
     *
     * @param <T>
     * @param arg
     * @param varname
     * @return
     * @throws IllegalArgumentException
     */
    @ReturnNonNull
    public static <T> T[] nonNullAll(T[] arg, String varname) throws IllegalArgumentException {
        nonNull(arg, varname);
        for (int i = 0; i < arg.length; i++) {
            if (arg[i] == null) {
                String msg = arrayToString(varname, "[", i, "] is must not be null.");
                throw new IllegalArgumentException(msg);
            }
        }
        return arg;
    }

    /**
     * コレクションの全項目が null でないことをチェックする.
     *
     * @param <T>
     * @param arg
     * @param varname
     * @return
     * @throws IllegalArgumentException
     */
    @ReturnNonNull
    public static <T extends Iterable> T nonNullAll(T arg, String varname) throws IllegalArgumentException {
        nonNull(arg, varname);
        int i = 0;
        for (Object x : arg) {
            if (x == null) {
                String msg = arrayToString(varname, "[", i, "] is must not be null.");
                throw new IllegalArgumentException(msg);
            }
            i++;
        }
        return arg;
    }

    private Arguments() {
    }
}
