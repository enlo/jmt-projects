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
package info.naiv.lab.java.jmt.infrastructure;

import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.springframework.beans.TypeMismatchException;

/**
 * 属性マップ
 *
 * @author enlo
 */
public interface Attributes {

    /**
     *
     * @return 保持している属性名の一覧
     */
    @Nonnull
    Set<String> getAttributeNames();

    /**
     * 属性名に対応する値を取得する. 値が存在しない場合は null を戻す.
     *
     * @param name 属性名.
     * @return 値. 属性が無い場合は null.
     */
    Object getAttribute(@Nonnull String name);

    /**
     * 属性名に対応する値を型付で取得する. 値が存在しない場合は null を戻す.
     *
     * @param <T> 値の型.
     * @param name 属性名.
     * @param clazz 型.
     * @throws TypeMismatchException 型と値が一致しない場合.
     * @return 値. 属性が無い場合は null.
     */
    <T> T getAttribute(@Nonnull String name, @Nonnull Class<T> clazz) throws TypeMismatchException;

    /**
     * 属性の有無を確認.
     *
     * @param name 属性名.
     * @return 指定された名前の属性名があれば true.
     */
    boolean contains(@Nonnull String name);

    /**
     * 属性の内容をマップにコピー
     *
     * @param target コピー先のマップ
     * @return target そのもの
     */
    @Nonnull
    Map<String, Object> copyTo(@Nonnull Map<String, Object> target);
}
