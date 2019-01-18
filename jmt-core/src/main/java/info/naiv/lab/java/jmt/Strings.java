/*
 * The MIT License
 *
 * Copyright 2018 enlo.
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

import static java.lang.Character.isSpaceChar;
import static java.lang.Character.isWhitespace;
import java.nio.CharBuffer;
import javax.annotation.Nonnull;

/**
 *
 * @author enlo
 */
public class Strings {

    @Nonnull
    public static String concatnate(String s1, String s2) {
        if (s1 == null) {
            return s2 != null ? s2 : "";
        }
        else if (s2 == null) {
            return s1;
        }
        else {
            return s1.concat(s2);
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
     * 中身が空白かどうかチェック.
     *
     * @param value チェックする文字列.
     * @return 空か空白のみなら true.
     * @see Character#isWhitespace(char)
     */
    public static boolean isBlank(CharSequence value) {
        if (isEmpty(value)) {
            return true;
        }
        else {
            int l = value.length();
            for (int i = 0; i < l; i++) {
                char ch = value.charAt(i);
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
     * @param value チェックするオブジェクト
     * @return 空ならば true.
     */
    public static boolean isEmpty(CharSequence value) {
        return value == null || value.length() == 0;
    }

    /**
     * 中身が空白でないことをチェック.
     *
     * @param value チェックする文字列
     * @return 空でも空白でなければ true.
     */
    public static boolean isNotBlank(CharSequence value) {
        return !isBlank(value);
    }

    /**
     * 中身があるかどうかチェック.
     *
     * @param value チェックするオブジェクト
     * @return 空でなければ true.
     */
    public static boolean isNotEmpty(CharSequence value) {
        return !isEmpty(value);
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

    @Nonnull
    public static String trimToEmpty(String str) {
        if (str == null) {
            return "";
        }
        else {
            return str.trim();
        }
    }

    public static String trimLeft(String str) {
        int len;
        if (str == null || (len = str.length()) == 0) {
            return "";
        }
        else {
            int i = 0;
            while ((i < len) && (str.charAt(i) <= ' ')) {
                i++;
            }
            if (0 < i) {
                return str.substring(i);
            }
            return str;
        }
    }
}
